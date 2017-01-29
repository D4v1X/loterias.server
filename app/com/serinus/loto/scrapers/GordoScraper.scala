package com.serinus.loto.scrapers

import java.net.URI
import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}
import java.util.Locale
import java.util.function.Consumer
import javax.inject.{Inject, Named}

import akka.actor.Actor
import com.serinus.loto._
import com.serinus.loto.scrapers.ScraperMessages.{ScrapGordo, ScrapHistoricGordo}
import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.{Constants, DB}
import org.jsoup.nodes.{Document, Element}
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Named(Constants.GORDO_SCRAPER_NAME)
class GordoScraper @Inject()(db: DB, lotteryService: LotteryService) extends Actor with GenericScraper {

  override def receive: PartialFunction[Any, Unit] = {
    case ScrapGordo => run
    case ScrapHistoricGordo(init, end) => runHistoric(init, end)
    case _ @ msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }


  /**
    * URL for the lottery we want to scrap
    */
  override protected val lotteryUrl: RaffleDate => URI = rd => {
    val parsedDate = rd.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    URI.create(s"http://elgordo.combinacionganadora.com/$parsedDate/")
  }


  /**
    * Database connection
    */
  override protected val getDB: DB = db


  /**
    * Parses the HTML contained in the URL specified and extracts any possible results
    *
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  override protected def resultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]] = {
    Logger.debug("Starting Gordo scraper")

    try {
      if (raffleResultAvailableForDate(doc, raffleDate)) {
        val combinationPartNames = List(Constants.TM_COMB_PART_GORDO_COMB_NAME, Constants.TM_COMB_PART_GORDO_REINT_NAME)
        val listFutureIds = combinationPartNames map lotteryService.getGordoCombinationPartIdWithName
        val futureIdList = Future.fold(listFutureIds)(ListBuffer.empty: ListBuffer[Integer])(_ += _)

        futureIdList map { combPartIds =>
          Right(generateGordoResults(doc, combPartIds, raffleDate))
        } recover {
          case err => Left(s"${err.getMessage}")
        }

      } else {
        Future(Left("There is no raffle result yet for Gordo"))
      }
    } catch {
      case e: Throwable =>
        Logger.error("There's been an error executing the Gordo parser", e)
        Future(Left("There's been an error executing the Gordo parser"))
    }

  }


  /**
    * Checks if the raffle result is available at the time of scraping
    *
    * @param doc The Jsoup HTML document
    * @param raffleDate the date to check for results
    * @return True if the raffle result is available or False otherwise
    */
  override protected def raffleResultAvailableForDate(doc: Document, raffleDate: RaffleDate): Boolean = {
    val dateHtmlElem = doc.select(".fld_gameDate").first()

    if (dateHtmlElem != null) {
      val parsedDateTime = DateTimeFormatter
        .ofPattern("EEEE, d 'de' MMMM 'de' uuuu")
        .withLocale(new Locale(Constants.SPANISH_LOCALE_CODE))
        .parse(dateHtmlElem.html())
      LocalDate.from(parsedDateTime) == raffleDate
    } else {
      Logger.error(s"Error trying to find the element with class <.fld_gameDate> in the html document.")
      false
    }
  }


  private def generateGordoResults(doc: Document,
                                   combPartIds: Seq[Integer],
                                   raffleDate: RaffleDate): Seq[ScrapResult] = {
    val winningResults = List(parseCombinacionGanadora(doc), parseReintCombGanadora(doc))
    (combPartIds zip winningResults).map(tuple => (raffleDate, tuple._1.toInt, tuple._2))
  }


  /**
    * Parses the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the winning number
    */
  def parseCombinacionGanadora(doc: Document): ResultValue ={
    Logger.debug("Parsing the winning number from the HTML document")

    var combinacionGanadora = ListBuffer[String]()
    doc.getElementsByClass("resultsWrapper").first().getElementsByTag("li").forEach(new Consumer[Element] {
      override def accept(t: Element) = combinacionGanadora += t.html()
    })

    combinacionGanadora.mkString(",")
  }


  /**
    * Parses the "Reintegro" for the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "Reintegro"
    */
  def parseReintCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number Reintegro from the HTML document")

    doc.select(".resultsAuxNumbersWrapper .ctrlNumbers:eq(0) dd").first().html()
  }


  /**
    * URI for the historic lottery page
    */
  override protected val historicLotteryUrl: RaffleDate => URI = lotteryUrl


  /**
    * Days when the lottery takes place
    */
  override protected val raffleWeekDays: Seq[DayOfWeek] = List(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)


  /**
    * Parses the HTML contained in the URI specified and extracts any possible historic results
    *
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  override protected def historicResultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]] =
    resultsParser(doc, raffleDate)
}

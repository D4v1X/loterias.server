package com.serinus.loto.scrapers

import java.net.URI
import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}
import java.util.Locale
import javax.inject.{Inject, Named}

import akka.actor.Actor
import com.serinus.loto.scrapers.ScraperMessages.{ScrapEuromillones, ScrapHistoricEuromillones}
import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.{Constants, DB}
import org.jsoup.nodes.Document
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Named(Constants.EUROMILLONES_SCRAPER_NAME)
class EuromillonesScraper @Inject()(db: DB, lotteryService: LotteryService) extends Actor with GenericScraper {

  override def receive: PartialFunction[Any, Unit] = {
    case ScrapEuromillones => run
    case ScrapHistoricEuromillones(initialDate, finalDate) => runHistoric(initialDate, finalDate)
    case _@msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }


  /**
    * Database connection
    */
  override protected val getDB: DB = db


  /**
    * URI for the lottery we want to scrap
    */
  override protected val lotteryUrl: RaffleDate => URI = rd => {
    val parsedDate = rd.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
    URI.create(s"http://euromillones.combinacionganadora.com/$parsedDate/")
  }


  /**
    * URI for the historic lottery page
    */
  override protected val historicLotteryUrl: RaffleDate => URI = lotteryUrl


  /**
    * Days when the lottery takes place
    */
  override protected val raffleWeekDays: Seq[DayOfWeek] = List(DayOfWeek.TUESDAY, DayOfWeek.FRIDAY)


  /**
    * Parses the HTML contained in the URI specified and extracts any possible historic results
    *
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  override protected def historicResultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]] =
    // we can use the same parser as the historic and current lottery URI's are the same except for the dates
    resultsParser(doc, raffleDate)


  /**
    * Parses the HTML contained in the URI specified and extracts any possible results
    *
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  override protected def resultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]] = {
    Logger.debug("Starting Euromillones scraper")

    try {
      if (raffleResultAvailableForDate(doc, raffleDate)) {
        val combinationPartNames = List(Constants.TM_COMB_PART_EUROMILLONES_COMB_NAME, Constants.TM_COMB_PART_EUROMILLONES_ESTRE_NAME, Constants.TM_COMB_PART_EUROMILLONES_MILLON_NAME)
        val listFutureIds = combinationPartNames map lotteryService.getEuromillonesCombinationPartIdWithName
        val futureIdList = Future.fold(listFutureIds)(ListBuffer.empty: ListBuffer[Integer])(_ += _)

        futureIdList map { combPartIds =>
          Right(generateEuromillonesResults(doc, combPartIds, raffleDate))
        } recover {
          case err => Left(s"${err.getMessage}")
        }

      } else {
        Future(Left("There is no raffle result yet for Euromillones"))
      }
    } catch {
      case e: Throwable =>
        Logger.error("There's been an error executing the Euromillones parser", e)
        Future(Left("There's been an error executing the Euromillones parser"))
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



  private def generateEuromillonesResults(doc: Document,
                                          combPartIds: Seq[Integer],
                                          raffleDate: RaffleDate): Seq[ScrapResult] = {
    var winningResults = ListBuffer(parseCombinacionGanadora(doc), parseEstrellasCombGanadora(doc))
    val availableCombPartIds = ListBuffer(combPartIds: _*)
    parseElmillonCombGanadora(doc) match {
      case Some(m) => winningResults += m
      case None => availableCombPartIds.remove(availableCombPartIds.size - 1)
    }
    (availableCombPartIds zip winningResults).map(tuple => (raffleDate, tuple._1.toInt, tuple._2))
  }


  /**
    * Parses the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the winning number
    */
  def parseCombinacionGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number from the HTML document")

    val combinacionGanadora = doc
      .getElementsByClass("resultsWrapper").first()
      .getElementsByClass("ctrlNumbers").first()
      .text()

    combinacionGanadora.replace(" ", ",")
  }


  /**
    * Parses the "Estrellas" for the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "Estrellas"
    */
  def parseEstrellasCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number Estrellas from the HTML document")

    val estrellasCombGanadora = doc
      .getElementsByClass("resultsAuxNumbersWrapper").first()
      .getElementsByClass("ctrlNumbers").first()
      .getElementsByTag("dd")
      .text()

    estrellasCombGanadora.replace(" ", ",")
  }


  /**
    * Parses the "El millon" for the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "El millon"
    */
  def parseElmillonCombGanadora(doc: Document): Option[ResultValue] = {
    Logger.debug("Parsing the winning number El millon from the HTML document")

    val millonHtmlElem = doc.select(".resultsAuxNumbersWrapper .ctrlNumbers dd").eq(2)
    if (!millonHtmlElem.isEmpty) {
      Some(millonHtmlElem.html())
    } else {
      None
    }
  }

}

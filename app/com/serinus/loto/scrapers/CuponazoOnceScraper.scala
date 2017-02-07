package com.serinus.loto.scrapers

import java.net.URI
import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}
import java.util.Locale
import javax.inject.{Inject, Named}

import akka.actor.Actor
import com.serinus.loto._
import com.serinus.loto.scrapers.ScraperMessages.{ScrapCuponazo, ScrapHistoricCuponazo}
import com.serinus.loto.services.lotteries.CuponazoOnceLotteryService
import com.serinus.loto.utils.{Constants, DB}
import org.jsoup.nodes.Document
import play.api.Logger

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future



@Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME)
class CuponazoOnceScraper @Inject() (db: DB, cuponazoOnceService: CuponazoOnceLotteryService) extends Actor with GenericScraper {


  def receive: PartialFunction[Any, Unit] = {
    case ScrapCuponazo => run
    case ScrapHistoricCuponazo(init, end) => runHistoric(init, end)
    case _ @ msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }


  override protected val getDB: DB = db


  override protected val lotteryUrl: RaffleDate => URI = rd =>
    URI.create(s"http://www.comprobarcupononce.es/cuponazo-once.php?del-dia=${rd.toString}")


  override protected def resultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]] = {
    Logger.debug("Starting Cuponazo Once scraper")

    try {
      if (raffleResultAvailableForDate(doc, raffleDate)) {
        val listFutureIds = getCombinationPartNames(doc) map cuponazoOnceService.findCombinationPartIdWithName
        val futureIdList = Future.fold(listFutureIds)(ListBuffer.empty: ListBuffer[Integer])(_ += _)

        futureIdList map { combPartIds =>
          Right(generateCuponazoResults(doc, combPartIds, raffleDate))
        } recover {
          case err => Left(s"${err.getMessage}")
        }

      } else {
        Future(Left("There is no raffle result yet for Cuponazo Once"))
      }
    } catch {
      case e: Throwable =>
        Logger.error("There's been an error executing the Cuponazo parser", e)
        Future(Left("There's been an error executing the Cuponazo parser"))
    }

  }


  /**
    * Checks if the raffle result is available at the time of scraping
    * @param doc The Jsoup HTML document
    * @param raffleDate the date to check for results
    * @return True if the raffle result is available or False otherwise
    */
  override protected def raffleResultAvailableForDate(doc: Document, raffleDate: RaffleDate): Boolean = {
    val htmlDateElem = doc.select("time").first()

    if (htmlDateElem != null) {
      val raffleDayString = htmlDateElem.html()
      val parsedDateTime = DateTimeFormatter
        .ofPattern("EEEE d 'de' MMMM 'de' uuuu")
        .withLocale(new Locale(Constants.SPANISH_LOCALE_CODE))
        .parse(raffleDayString.toLowerCase)

      LocalDate.from(parsedDateTime) == raffleDate
    } else {
      Logger.error(s"Error trying to extract the <time> element from the html document.")
      false
    }
  }


  /**
    * Delivers the ordered list of combination parts used in the Cuponazo
    * @return List of strings containing the combination part names for the Cuponazo
    */
  private def getCombinationPartNames(doc: Document): List[String] = {
    val combPartNames = ListBuffer(
      Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA,
      Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_SERIE,
      Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_REINT
    )

    (1 to getAdditionalNumbersSize(doc)) foreach { an =>
      combPartNames += s"Número Adicional $an"
      combPartNames += s"Serie Número Adicional $an"
    }

    combPartNames.toList
  }


  private def getAdditionalNumbersSize(doc: Document): Int = {
    val size = doc.select("#adicionales table tbody tr").size()
    Logger.debug(s"The number of Cuponazo additional numbers for today is $size")
    size
  }



  /**
    * Generates the list of Cuponazo results
    * @param doc the JSoup HTML document
    * @param combPartIds the ordered list of TmCombinationPart identifiers for each one of the results associated with the Cuponazo
    * @return the list of Cuponazo results
    */
  private def generateCuponazoResults(doc: Document,
                                      combPartIds: Seq[Integer],
                                      raffleDate: RaffleDate): Seq[ScrapResult] = {
    val firstWinningResults = List(parseCombinacionGanadora(doc), parseSerieCombGanadora(doc), parseReintCombGanadora(doc))

    val cuponazoResults = firstWinningResults ++ parseAdditionalNumbersAndSeries(doc)

    (combPartIds zip cuponazoResults).map(tuple => (raffleDate, tuple._1.toInt, tuple._2))
  }



  /**
    * Parses the winning number
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the winning number
    */
  private def parseCombinacionGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number from the HTML document")

    val numberAndSeries = doc.select(".text-grb").first().html()
    val number = numberAndSeries.split("-")(0).trim

    number.toArray.mkString(",")
  }



  /**
    * Parses the series for the winning number
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the series
    */
  private def parseSerieCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number series from the HTML document")

    val numberAndSeries = doc.select(".text-grb").first().html()
    numberAndSeries.split("-")(1).trim
  }



  /**
    * Parses the "Reintegro" for the winning number
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "Reintegro"
    */
  private def parseReintCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number's Reintegro from the HTML document")

    val numberAndSeries = doc.select(".text-grb").first().html()
    val number = numberAndSeries.split("-")(0).trim

    number.reverse.substring(0, 1)
  }


  /**
    * Parses the additional winning numbers and series
    * @param doc the JSoup HTML document
    * @return the list of ResultValues corresponding with the additional numbers and series in order
    */
  private def parseAdditionalNumbersAndSeries(doc: Document): Seq[ResultValue] = {
    Logger.debug("Parsing the additional winning numbers and series from the HTML document")

    var additionalNumberAndSeries = ListBuffer[ResultValue]()
    val additionalNumbersSize = doc.select("#adicionales table tbody tr").size()

    0 until additionalNumbersSize foreach { number =>
      val additionalNumber = doc.select(s"#adicionales table tbody tr:eq($number) td:eq(0)").first().html()
      val additionalSerie = doc.select(s"#adicionales table tbody tr:eq($number) td:eq(1)").first().html()

      Logger.debug(s"CuponazoOnceScraper processing additional number $additionalNumber and series $additionalSerie")

      additionalNumberAndSeries += additionalNumber.toArray.mkString(",")
      additionalNumberAndSeries += additionalSerie
    }

    additionalNumberAndSeries
  }


  /**
    * URI for the historic lottery page given a raffle date
    */
  override protected val historicLotteryUrl: RaffleDate => URI = lotteryUrl


  /**
    * Days when the lottery takes place
    */
  override protected val raffleWeekDays: Seq[DayOfWeek] = List(DayOfWeek.FRIDAY)


  /**
    * Parses the HTML contained in the URI specified and extracts any possible historic results
    *
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  override protected def historicResultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]] =
    // we can use the same parser as the historic and current lottery URI's are the same except for the dates
    resultsParser(doc, raffleDate)

}

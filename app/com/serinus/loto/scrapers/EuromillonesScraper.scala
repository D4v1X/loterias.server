package com.serinus.loto.scrapers

import java.net.URI
import java.time.format.DateTimeFormatter
import java.time.{DayOfWeek, LocalDate}
import java.util.Locale
import javax.inject.{Inject, Named}

import akka.actor.Actor
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
    case ScraperMessages.ScrapEuromillones => run
    case ScraperMessages.ScrapHistoricEuromillones(initialDate, finalDate) => runHistoric(initialDate, finalDate)
    case _@msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }

  /**
    * Database connection
    */
  override protected val getDB: DB = db

  /**
    * URI for the lottery we want to scrap
    */
  override protected val lotteryUrl: (RaffleDate) => URI = _ =>
    URI.create(s"http://euromillones.combinacionganadora.com/2014/07/08/")

  /**
    * URI for the historic lottery page
    */
  override protected val historicLotteryUrl: (RaffleDate) => URI = rd =>
    URI.create(s"http://euromillones.combinacionganadora.com/${rd.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))}")

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

    if (raffleResultAvailableForDate(doc, raffleDate)) {
      for {

      // Combinacion ganadora
        partCombGanadoraId <- lotteryService.getEuromillonesCombinationPartIdWithName(
          Constants.TM_COMB_PART_EUROMILLONES_COMB_NAME)

        // Estrellas
        partCombGanadoraEstrellasId <- lotteryService.getEuromillonesCombinationPartIdWithName(
          Constants.TM_COMB_PART_EUROMILLONES_ESTRE_NAME)

        // El Millon
        partCombGanadoraElmillonId <- lotteryService.getEuromillonesCombinationPartIdWithName(
          Constants.TM_COMB_PART_EUROMILLONES_MILLON_NAME)

      } yield {

        var results = new ListBuffer[ScrapResult]()

        val combGanadoraValue = parseCombinacionGanadora(doc)
        results += ((raffleDate, partCombGanadoraId, combGanadoraValue))

        val estrellasCombGanadora = parseEstrellasCombGanadora(doc)
        results += ((raffleDate, partCombGanadoraEstrellasId, estrellasCombGanadora))

        if (elmillonWasCelebrated(raffleDate)) {
          val elmillonCombGanadora = parseElmillonCombGanadora(doc)
          results += ((raffleDate, partCombGanadoraElmillonId, elmillonCombGanadora))
        }

        Right(results)

      }
    } else {

      Future(Left("There is no raffle result yet for Euromillones"))

    }
  }

  def elmillonWasCelebrated(raffleDate: RaffleDate) : Boolean = {
    val elmillonBeganDate = LocalDate.of(2016, 9, 27)
    raffleDate.getDayOfWeek == DayOfWeek.FRIDAY && raffleDate.isAfter(elmillonBeganDate)
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
  def parseElmillonCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number El millon from the HTML document")

    doc
      .getElementsByClass("resultsAuxNumbersWrapper").first()
      .getElementsByClass("ctrlNumbers").last()
      .getElementsByTag("dd")
      .text()
  }

  /**
    * Checks if the raffle result is available at the time of scraping
    *
    * @param doc        The Jsoup HTML document
    * @param raffleDate the date to check for results
    * @return True if the raffle result is available or False otherwise
    */
  override protected def raffleResultAvailableForDate(doc: Document, raffleDate: RaffleDate): Boolean = {
    val raffleDayString = doc.getElementsByClass("fld_gameDate").first().text()

    val parsedDateTime = DateTimeFormatter
      .ofPattern("EEEE',' d 'de' MMMM 'de' uuuu")
      .withLocale(new Locale(Constants.SPANISH_LOCALE_CODE))
      .parse(raffleDayString)

    LocalDate.from(parsedDateTime) == raffleDate
  }
}

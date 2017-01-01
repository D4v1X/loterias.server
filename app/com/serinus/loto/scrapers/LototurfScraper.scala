package com.serinus.loto.scrapers

import java.net.URI
import java.time.{DayOfWeek, LocalDate}
import java.time.format.DateTimeFormatter
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

@Named(Constants.LOTOTURF_SCRAPER_NAME)
class LototurfScraper @Inject()(db: DB, lotteryService: LotteryService) extends Actor with GenericScraper {

  override def receive: PartialFunction[Any, Unit] = {
    case ScraperMessages.ScrapLototurf => run
    case _@msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }

  /**
    * URL for the lottery we want to scrap
    */
  override protected val lotteryUrl: RaffleDate => URI = _ =>
    URI.create("http://www.loteriasyapuestas.es/es/lototurf")

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
    Logger.debug("Starting Lototurf scraper")

    if (raffleResultAvailableForDate(doc, raffleDate)) {
      for {

        // Combinacion ganadora
        partCombGanadoraId <- lotteryService.getLototurfCombinationPartIdWithName(
          Constants.TM_COMB_PART_LOTOTURF_COMB_NAME)

        // Caballo
        partCombGanadoraCaballoId <- lotteryService.getLototurfCombinationPartIdWithName(
          Constants.TM_COMB_PART_LOTOTURF_CABALLO_NAME)

        // Reintegro
        partCombGanadoraReintId <- lotteryService.getLototurfCombinationPartIdWithName(
          Constants.TM_COMB_PART_LOTOTURF_REINT_NAME)


      } yield {

        var results = new ListBuffer[ScrapResult]()
        val raffleDay = LocalDate.now()

        val combGanadoraValue = parseCombinacionGanadora(doc)
        results += ((raffleDay, partCombGanadoraId, combGanadoraValue))

        val caballoCombGanadora = parseCaballoCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraCaballoId, caballoCombGanadora))

        val reintCombGanadora = parseReintCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraReintId, reintCombGanadora))

        Right(results)

      }
    } else {

      Future(Left("There is no raffle result yet for LotoTurf"))

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
    val datePattern = "\\d{2}\\/\\d{2}\\/\\d{4}".r
    val raffleDayString = datePattern.findFirstIn(doc.getElementById("lastResultsTitleLink").text()).get

    val parsedDateTime = DateTimeFormatter
      .ofPattern("dd/MM/yyyy")
      .withLocale(new Locale(Constants.SPANISH_LOCALE_CODE))
      .parse(raffleDayString)

    LocalDate.from(parsedDateTime) == LocalDate.now()
  }

  /**
    * Parses the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the winning number
    */
  def parseCombinacionGanadora(doc: Document): ResultValue ={
    Logger.debug("Parsing the winning number from the HTML document")

    val combinacionGanadora = doc.getElementsByClass("cuerpoRegionIzq").first().getElementsByTag("li").text()

    combinacionGanadora.replace(" ", ",")
  }

  /**
    * Parses the "Caballo" for the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "Caballo"
    */
  def parseCaballoCombGanadora(doc: Document): ResultValue ={
    Logger.debug("Parsing the winning number Caballo from the HTML document")

    doc.getElementsByClass("cuerpoRegionDerecha").first().getElementsByClass("bolaPeq").first().text()
  }

  /**
    * Parses the "Reintegro" for the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "Reintegro"
    */
  def parseReintCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number Reintegro from the HTML document")

    doc.getElementsByClass("cuerpoRegionDerecha").first().getElementsByClass("bolaPeq").last().text()
  }

  /**
    * URI for the historic lottery page
    */
  override protected val historicLotteryUrl: (RaffleDate) => URI = ???
  /**
    * Days when the lottery takes place
    */
  override protected val raffleWeekDays: Seq[DayOfWeek] = ???

  /**
    * Parses the HTML contained in the URI specified and extracts any possible historic results
    *
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  override protected def historicResultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]] = ???
}

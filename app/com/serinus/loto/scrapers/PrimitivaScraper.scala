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


@Named(Constants.PRIMITIVA_SCRAPER_NAME)
class PrimitivaScraper @Inject() (db: DB, lotteryService: LotteryService) extends Actor with GenericScraper {


  def receive: PartialFunction[Any, Unit] = {
    case ScraperMessages.ScrapPrimitiva => run
    case _ @ msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }


  /**
    * URL for the lottery we want to scrap
    */
  override protected val lotteryUrl: RaffleDate => URI = _ =>
    URI.create("http://www.loteriasyapuestas.es/es/la-primitiva")

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
    Logger.debug("Starting Primitiva scraper")

    if (raffleResultAvailableForDate(doc, raffleDate)) {
      for {

      // Combinacion ganadora
        partCombGanadoraId <- lotteryService.getPrimitivaCombinationPartIdWithName(
          Constants.TM_COMB_PART_PRIMITIVA_COMB_NAME)

        // Complementario
        partCombGanadoraComplementarioId <- lotteryService.getPrimitivaCombinationPartIdWithName(
          Constants.TM_COMB_PART_PRIMITIVA_COMPL_NAME)

        // Reintegro
        partCombGanadoraReintId <- lotteryService.getPrimitivaCombinationPartIdWithName(
          Constants.TM_COMB_PART_PRIMITIVA_REINT_NAME)

        //Joker
        partCombGanadoraJokerId <- lotteryService.getPrimitivaCombinationPartIdWithName(
          Constants.TM_COMB_PART_PRIMITIVA_JOKER_NAME)

      } yield {

        var results = new ListBuffer[ScrapResult]()
        val raffleDay = LocalDate.now()

        val combGanadoraValue = parseCombinacionGanadora(doc)
        results += ((raffleDay, partCombGanadoraId, combGanadoraValue))

        val complementarioCombGanadora = parseComplementarioCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraComplementarioId, complementarioCombGanadora))

        val reintCombGanadora = parseReintCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraReintId, reintCombGanadora))

        val jokerCombGanadora = parseJokerCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraJokerId, jokerCombGanadora))

        Right(results)

      }
    } else {

      Future(Left("There is no raffle result yet for Primitiva"))

    }
  }

  /**
    * Checks if the raffle result is available at the time of scraping
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

    LocalDate.from(parsedDateTime) == raffleDate
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
    * Parses the "Complementario" for the winning number
    *
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "Complementario"
    */
  def parseComplementarioCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number Complementario from the HTML document")

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
    * Parses the Joker for the winning number
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the Joker
    */
  def parseJokerCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number Joker from the HTML document")

    val joker = doc.getElementsByClass("joker").first().text()

    joker.replace(" ", "")
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

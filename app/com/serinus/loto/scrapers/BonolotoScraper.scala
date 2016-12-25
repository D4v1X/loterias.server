package com.serinus.loto.scrapers

import java.time.LocalDate
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

@Named(Constants.BONOLOTO_SCRAPER_NAME)
class BonolotoScraper @Inject()(db: DB, lotteryService: LotteryService) extends Actor with GenericScraper {

  override def receive: PartialFunction[Any, Unit] = {
    case ScraperMessages.ScrapBonoloto => run
    case _ @ msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }

  /**
    * URL for the lottery we want to scrap
    */
  override protected val lotteryUrl: String = "http://www.loteriasyapuestas.es/es/bonoloto"

  /**
    * Database connection
    */
  override protected val getDB: DB = db

  /**
    * Parses the HTML contained in the URL specified and extracts any possible results
    *
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  override protected def twResultParser: (Document) => Future[Either[ScrapError, Seq[ScrapResult]]] = { doc =>
    Logger.debug("Starting Bonoloto scraper")

    if (todaysRaffleResultAvailable(doc)) {
      for {

        // Combinacion ganadora
        partCombGanadoraId <- lotteryService.getBonolotoCombinationPartIdWithName(
          Constants.TM_COMB_PART_BONOLOTO_COMB_NAME)

        // Complementario
        partCombGanadoraComplementarioId <- lotteryService.getBonolotoCombinationPartIdWithName(
          Constants.TM_COMB_PART_BONOLOTO_COMPL_NAME)

        // Reintegro
        partCombGanadoraReintId <- lotteryService.getBonolotoCombinationPartIdWithName(
          Constants.TM_COMB_PART_BONOLOTO_REINT_NAME)


      } yield {

        var results = new ListBuffer[ScrapResult]()
        val raffleDay = LocalDate.now()

        val combGanadoraValue = parseCombinacionGanadora(doc)
        results += ((raffleDay, partCombGanadoraId, combGanadoraValue))

        val complementarioCombGanadora = parseComplementarioCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraComplementarioId, complementarioCombGanadora))

        val reintCombGanadora = parseReintCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraReintId, reintCombGanadora))

        Right(results)

      }
    } else {

      Future(Left("There is no raffle result yet for Bonoloto"))

    }
  }

  /**
    * Checks if the raffle result is available at the time of scraping
    *
    * @param doc The Jsoup HTML document
    * @return True if the raffle result is available or False otherwise
    */
  def todaysRaffleResultAvailable(doc: Document): Boolean = {
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

}
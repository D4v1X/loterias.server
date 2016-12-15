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



@Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME)
class CuponazoOnceScraper @Inject() (db: DB, lotteryService: LotteryService) extends Actor with GenericScraper {


  def receive: PartialFunction[Any, Unit] = {
    case ScraperMessages.ScrapCuponazo => run
  }


  override protected val getDB: DB = db


  override protected val lotteryUrl: String = "https://www.juegosonce.es/resultados-cuponazo"


  override protected def twResultParser: (Document) => Future[Either[ScrapError, Seq[ScrapResult]]] = { doc =>

    Logger.debug("Starting Cuponazo Once scraper")

    if (todaysRaffleResultAvailable(doc)) {
      for {

        // winning number
        partCombGanadoraId <- lotteryService.getCuponazoCombinationPartIdWithName(
          Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA)

        // serie
        partCombGanadoraSerieId <- lotteryService.getCuponazoCombinationPartIdWithName(
          Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_SERIE)

        // reintegro
        partCombGanadoraReintId <- lotteryService.getCuponazoCombinationPartIdWithName(
          Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_REINT)

      } yield {

        var results = new ListBuffer[ScrapResult]()
        val raffleDay = LocalDate.now()

        val combGanadoraValue = parseCombinacionGanadora(doc)
        results += ((raffleDay, partCombGanadoraId, combGanadoraValue))

        val serieCombGanadora = parseSerieCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraSerieId, serieCombGanadora))

        val reintCombGanadora = parseReintCombGanadora(doc)
        results += ((raffleDay, partCombGanadoraReintId, reintCombGanadora))

        Right(results)

      }
    } else {

      Future(Left("There is no raffle result yet for Cuponazo Once"))

    }
  }


  /**
    * Checks if the raffle result is available at the time of scraping
    * @param doc The Jsoup HTML document
    * @return True if the raffle result is available or False otherwise
    */
  private def todaysRaffleResultAvailable(doc: Document): Boolean = {
    val raffleDayString = doc.select(".escrutinio span:eq(1)").first().html()

    val parsedDateTime = DateTimeFormatter
      .ofPattern("EEEE, d 'de' MMMM 'de' uuuu")
      .withLocale(new Locale(Constants.SPANISH_LOCALE_CODE))
      .parse(raffleDayString)

    LocalDate.from(parsedDateTime) == LocalDate.now()
  }


  /**
    * Parses the winning number
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the winning number
    */
  private def parseCombinacionGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number from the HTML document")

    val number = doc.select(".numerocupon").first().html()

    number.toArray.mkString(",")
  }


  /**
    * Parses the series for the winning number
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the series
    */
  private def parseSerieCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number series from the HTML document")

    val textWithSerie = doc.select(".numerocupon").first().parent().parent().text()

    textWithSerie.substring(textWithSerie.lastIndexOf(":") + 1).trim()
  }


  /**
    * Parses the "Reintegro" for the winning number
    * @param doc the Jsoup HTML document
    * @return the ResultValue containing the "Reintegro"
    */
  private def parseReintCombGanadora(doc: Document): ResultValue = {
    Logger.debug("Parsing the winning number Reintegro from the HTML document")

    val number = doc.select(".numerocupon").first().html()

    number.reverse.substring(0, 1)
  }

}

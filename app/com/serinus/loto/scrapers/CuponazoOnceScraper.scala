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
    case _ @ msg => Logger.warn(s"${this.getClass.getName} received unrecognized message $msg")
  }


  override protected val getDB: DB = db


  override protected val lotteryUrl: String = "https://www.juegosonce.es/resultados-cuponazo"


  override protected def twResultParser: (Document) => Future[Either[ScrapError, Seq[ScrapResult]]] = { doc =>
    Logger.debug("Starting Cuponazo Once scraper")

    if (todaysRaffleResultAvailable(doc)) {

      val listFutureIds = getCombinationPartNames map lotteryService.getCuponazoCombinationPartIdWithName

      val futureIdList = Future.fold(listFutureIds)(ListBuffer.empty: ListBuffer[Integer])(_ += _)

      futureIdList map { combPartIds =>
        Right(generateCuponazoResults(doc, combPartIds))
      } recover {
        case e => Left(s"There's been an error trying to retrive the cuponazo raffle results: ${e.getMessage}")
      }

    } else {

      Future(Left("There is no raffle result yet for Cuponazo Once"))

    }
  }


  /**
    * Generates the list of Cuponazo results
    * @param doc the JSoup HTML document
    * @param combPartIds the ordered list of TmCombinationPart identifiers for each one of the results associated with the Cuponazo
    * @return the list of Cuponazo results
    */
  private def generateCuponazoResults(doc: Document,
                                      combPartIds: Seq[Integer]): Seq[ScrapResult] = {
    val raffleDay = LocalDate.now()

    val firstWininngResults = List(parseCombinacionGanadora(doc), parseSerieCombGanadora(doc), parseReintCombGanadora(doc))

    val cuponazoResults = firstWininngResults ++ parseAdditionalNumbersAndSeries(doc)

    (combPartIds zip cuponazoResults).map(tuple => (raffleDay, tuple._1.toInt, tuple._2))
  }


  /**
    * Delivers the ordered list of combination parts used in the Cuponazo
    * @return List of strings containing the combination part names for the Cuponazo
    */
  private def getCombinationPartNames: List[String] = {
    List(
      Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA,
      Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_SERIE,
      Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_REINT,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_1,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_1_SERIE,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_2,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_2_SERIE,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_3,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_3_SERIE,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_4,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_4_SERIE,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_5,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_5_SERIE,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_6,
      Constants.TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_6_SERIE
    )
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


  /**
    * Parses the additional winning numbers and series
    * @param doc the JSoup HTML document
    * @return the list of ResultValues corresponding with the additional numbers and series in order
    */
  private def parseAdditionalNumbersAndSeries(doc: Document): Seq[ResultValue] = {
    Logger.debug("Parsing the additional winning numbers and series from the HTML document")

    var additionalNumberAndSeries = ListBuffer[ResultValue]()

    0 to 5 foreach(number => {
      val additionalNumber = doc.select(s".columnas li:eq($number) .numero").html()
      val additionalSerie = doc.select(s".columnas li:eq($number) .serie").html()

      additionalNumberAndSeries += additionalNumber.toArray.mkString(",")
      additionalNumberAndSeries += additionalSerie
    })

    additionalNumberAndSeries
  }

}

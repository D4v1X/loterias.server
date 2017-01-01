package com.serinus.loto.scrapers

import java.net.URI
import java.time.{DayOfWeek, Duration, LocalDate}
import java.time.temporal.{ChronoUnit, TemporalAdjusters}

import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.utils.DB
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait GenericScraper {


  private def getHtmlPageAt(uri: URI): Document = {
    Jsoup.connect(uri.toString).get()
  }


  /**
    * Scraps the lottery URL given and saves the results, if any, in the database
    *
      */
  private def scrapUrl: Future[Unit] = {
    val doc = getHtmlPageAt(lotteryUrl apply LocalDate.now)

    val futureResult = resultsParser(doc, LocalDate.now)

    futureResult map {

      case Left(e) => Logger.error(e)

      case Right(res) => saveResults(res)

    }

  }


  /**
    * Saves the results scraped from the URL into the database
    * @param results the list of ScrapResult tuples to store in the database
    */
  private def saveResults(results: Seq[ScrapResult]): Unit = {
    getDB.withTransaction { db =>

      Logger.debug("About the save the results scraped into the database")

      results
        .foreach(tuple => {

          db
            .insertInto(Tables.TW_RESULT)
            .columns(Tables.TW_RESULT.RAFFLE_DAY, Tables.TW_RESULT.COMBINATION_PART_ID, Tables.TW_RESULT.VALUES)
            .values(tuple._1, tuple._2, tuple._3)
            .execute()

        })

      Logger.debug("Results successfully stored in the database. Keep'em coming.")

    }.onFailure {

      case error => Logger.error(s"Error trying to save the lottery results: ${error.getMessage}")

    }
  }


  /**
    * URI for the lottery we want to scrap
    */
  protected val lotteryUrl: RaffleDate => URI


  /**
    * URI for the historic lottery page
    */
  protected val historicLotteryUrl: RaffleDate => URI


  /**
    * Database connection
    */
  protected val getDB: DB


  /**
    * Days when the lottery takes place
    */
  protected val raffleWeekDays: Seq[DayOfWeek]


  /**
    * Parses the HTML contained in the URI specified and extracts any possible results
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  protected def resultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]]


  /**
    * Parses the HTML contained in the URI specified and extracts any possible historic results
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  protected def historicResultsParser(doc: Document, raffleDate: RaffleDate): Future[Either[ScrapError, ScrapResultList]]


  /**
    * Checks if the raffle result is available at the time of scraping
    *
    * @param doc The Jsoup HTML document
    * @param raffleDate the date to check for results
    * @return True if the raffle result is available or False otherwise
    */
  protected def raffleResultAvailableForDate(doc: Document, raffleDate: RaffleDate): Boolean



  /**
    * This is the method to execute a given lottery scraper
    *
    */
  def run: Future[Unit] = {
    scrapUrl
  }


  /**
    * This method executes the historic scraper
    *
    * @param maybeInitialScrapDate The initial date to scrap (Optional). Defaults to yesterday
    * @param maybeFinalScrapDate The final date to scrap (Optional). Defaults to the current day
    */
  def runHistoric(maybeInitialScrapDate: Option[RaffleDate],
                  maybeFinalScrapDate: Option[RaffleDate]): Unit = {

    val initialScrapDate = maybeInitialScrapDate getOrElse LocalDate.now.minus(1, ChronoUnit.DAYS)
    val finalScrapDate = maybeFinalScrapDate getOrElse LocalDate.now

    0 until ChronoUnit.DAYS.between(initialScrapDate, finalScrapDate).toInt foreach { day =>
      val nextDate = initialScrapDate.plus(day, ChronoUnit.DAYS)

      if (raffleWeekDays.sorted contains nextDate.getDayOfWeek) {
        val htmlDoc = getHtmlPageAt(historicLotteryUrl apply nextDate)
        historicResultsParser(htmlDoc, nextDate) map {

          case Left(err) => Logger.error(s"${this.getClass.getName} error parsing raffle results for date $nextDate: $err")

          case Right(res) => saveResults(res)

        }

      }
    }
  }


}

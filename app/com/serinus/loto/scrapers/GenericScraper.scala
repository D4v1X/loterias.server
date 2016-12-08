package com.serinus.loto.scrapers

import java.time.LocalDate

import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.utils.DB
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


trait GenericScraper {

  type RaffleDate = LocalDate
  type CombinationPartId = Int
  type ResultValue = String
  type ScrapResult = (RaffleDate, CombinationPartId, ResultValue)
  type ScrapError = String


  /**
    * Scraps the lottery URL given and saves the results, if any, in the database
    *
    */
  private def scrapUrl: Future[Unit] = {
    val doc = Jsoup.connect(lotteryUrl).get()

    val futureResult = twResultParser apply doc

    futureResult map {

      case Left(e: ScrapError) => Logger.error(e)

      case Right(resultList: Seq[ScrapResult]) => saveResults(resultList)

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
    * URL for the lottery we want to scrap
    */
  protected val lotteryUrl: String


  /**
    * Database connection
    */
  protected val getDB: DB


  /**
    * Parses the HTML contained in the URL specified and extracts any possible results
    * @return a future containing either an error (if something wrong happened) or a sequence of results
    */
  protected def twResultParser : Document => Future[Either[ScrapError, Seq[ScrapResult]]]


  /**
    * This is the method to execute a given lottery scraper
    *
    */
  def run: Future[Unit] = {
    scrapUrl
  }

}

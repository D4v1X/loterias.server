package com.serinus.loto.scrapers

import java.time.LocalDate

import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.model.pojos.TwResult
import com.serinus.loto.utils.DB
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

trait GenericScraper {

  private def scrapUrl = Future {
    val doc = Jsoup.connect(url).get()

    val resultRecords = twResultParser apply doc

    resultRecords map saveResults
  }


  private def saveResults(resultRecords: Seq[TwResult]) = {
    getDB.withTransaction { db =>

      resultRecords
        .map(record => (record.getRaffleDay, record.getCombinationPartId, record.getValues))
        .foreach((tuple: (LocalDate, Integer, String)) => {

          db
            .insertInto(Tables.TW_RESULT)
            .columns(Tables.TW_RESULT.RAFFLE_DAY, Tables.TW_RESULT.COMBINATION_PART_ID, Tables.TW_RESULT.VALUES)
            .values(tuple._1, tuple._2, tuple._3)
            .execute()

        })

    }.onFailure {

      case error => Logger.error(s"Error trying to save the lottery results: ${error.getMessage}")

    }
  }

  protected val url: String

  protected val getDB: DB

  protected def twResultParser : Document => Future[Seq[TwResult]]

  def run = {
    scrapUrl
  }

}

package com.serinus.loto.scrapers
import java.time.LocalDate
import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.model.pojos.TwResult
import com.serinus.loto.utils.{Constants, DB}
import org.jsoup.nodes.Document

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class CuponazoOnceScraper @Inject() (db: DB) extends GenericScraper {

  override protected val getDB: DB = db

  override protected val url: String = "https://www.juegosonce.es/resultados-cuponazo"

  override protected def twResultParser: (Document) => Future[Seq[TwResult]] = { doc =>

    val resultRecordsLst = for {

      // winning number
      partCombGanadoraId <- getCuponazoCombinationPartIdWithName(
        Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA)

      // serie
      partCombGanadoraSerieId <- getCuponazoCombinationPartIdWithName(
        Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_SERIE)

      // reintegro
      partCombGanadoraReintId <- getCuponazoCombinationPartIdWithName(
        Constants.TM_COMB_PART_CUPONAZO_COMB_GANADORA_REINT)

    } yield {

      var results = new ListBuffer[TwResult]()
      val raffleDay = LocalDate.now()

      val combGanadoraValue = parseCombinacionGanadora(doc)
      results += new TwResult(null, raffleDay, partCombGanadoraId, combGanadoraValue, null)

      val serieCombGanadora = parseSerieCombGanadora(doc)
      results += new TwResult(null, raffleDay, partCombGanadoraSerieId, serieCombGanadora, null)

      val reintCombGanadora = parseReintCombGanadora(doc)
      results += new TwResult(null, raffleDay, partCombGanadoraReintId, reintCombGanadora, null)

    }

    resultRecordsLst

  }


  private def parseCombinacionGanadora(doc: Document): String = {
    val number = doc.select(".numerocupon").first().html()

    number.toArray.mkString(",")
  }


  private def parseSerieCombGanadora(doc: Document): String = {
    val textWithSerie = doc.select(".numerocupon").first().parent().parent().text()

    textWithSerie.substring(textWithSerie.lastIndexOf(":") + 1).trim
  }


  private def parseReintCombGanadora(doc: Document): String = {
    val number = doc.select(".numerocupon").first().html()

    number.reverse.substring(0, 1)
  }


  private def getCuponazoCombinationPartIdWithName(name: String) = {
    db.query { db =>

      val tmCombPartCuponazoCombGanadora = db
        .select(Tables.TM_COMBINATION_PART.ID)
        .from(Tables.TM_COMBINATION_PART)
        .innerJoin(Tables.TM_LOTTERY).on(Tables.TM_LOTTERY.ID.eq(Tables.TM_COMBINATION_PART.LOTTERY_ID))
        .where(Tables.TM_LOTTERY.NAME.eq(Constants.TM_LOTTERY_CUPONAZO_ONCE_NAME))
        .and(Tables.TM_COMBINATION_PART.NAME.eq(name))
        .fetchOne().value1()

      tmCombPartCuponazoCombGanadora

    }
  }

}

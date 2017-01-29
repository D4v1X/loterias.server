package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables._
import com.serinus.loto.utils.{Constants, DB}

import scala.collection.JavaConversions._
import scala.concurrent.Future

class PrimitivaStatsService @Inject() (db: DB) {

  def findAllPrimitivaMainResults(): Future[Seq[String]] = {

    db.query( db => {

      val results = db.select(TW_RESULT.VALUES)
        .from(TW_RESULT)
        .innerJoin(TM_COMBINATION_PART).on(TM_COMBINATION_PART.ID.eq(TW_RESULT.COMBINATION_PART_ID))
        .innerJoin(TM_LOTTERY).on(TM_LOTTERY.ID.eq(TM_COMBINATION_PART.LOTTERY_ID))
        .where(TM_LOTTERY.NAME.eq(Constants.TM_LOTTERY_PRIMITIVA_NAME))
        .and(TM_COMBINATION_PART.NAME.eq(Constants.TM_COMB_PART_PRIMITIVA_COMB_NAME))
        .fetch()
        .getValues(TW_RESULT.VALUES)

      results

    })

  }

}

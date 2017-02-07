package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables.{TM_COMBINATION_PART, TM_LOTTERY, TW_RESULT}
import com.serinus.loto.utils.DB
import com.serinus.loto.{CombinationPartName, LotteryName}

import scala.concurrent.Future

import scala.collection.JavaConversions._


class StatsService @Inject() (db: DB) {

  def findAllTheResultsOfACombinationPartNameOfALottery(lotteryName: LotteryName, combinationPartName: CombinationPartName): Future[Seq[String]] = {

    db.query( db => {

      val results = db.select(TW_RESULT.VALUES)
        .from(TW_RESULT)
        .innerJoin(TM_COMBINATION_PART).on(TM_COMBINATION_PART.ID.eq(TW_RESULT.COMBINATION_PART_ID))
        .innerJoin(TM_LOTTERY).on(TM_LOTTERY.ID.eq(TM_COMBINATION_PART.LOTTERY_ID))
        .where(TM_LOTTERY.NAME.eq(lotteryName))
        .and(TM_COMBINATION_PART.NAME.eq(combinationPartName))
        .fetch()
        .getValues(TW_RESULT.VALUES)

      results

    })

  }

}
package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.exceptions.DBException
import com.serinus.loto.model.caseclasses.rest.{CombinationPartRestCC, ResultRestCC}
import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.model.jooq.Tables.{TM_COMBINATION_PART, TM_LOTTERY, TW_RESULT}
import com.serinus.loto.utils.{Constants, DB}
import org.jooq.impl.DSL.max

import scala.collection.JavaConversions._
import scala.concurrent.Future

class LotteryService @Inject() (db: DB) {

  def getLototurfCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_LOTOTURF_NAME, name)
  }

  def getEuromillonesCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_EUROMILLONES_NAME, name)
  }

  def getGordoCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_GORDO_NAME, name)
  }

  def getBonolotoCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_BONOLOTO_NAME, name)
  }

  def getPrimitivaCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_PRIMITIVA_NAME, name)
  }

  def getCuponazoCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_CUPONAZO_ONCE_NAME, name)
  }

  private def getLotteryCombinationPartIdWithName(lotteryName: String, combinationPartName: String): Future[Integer] = {
    db.query { db =>

      val tmCombinationPartId = db
        .select(Tables.TM_COMBINATION_PART.ID)
        .from(Tables.TM_COMBINATION_PART)
        .innerJoin(Tables.TM_LOTTERY).on(Tables.TM_LOTTERY.ID.eq(Tables.TM_COMBINATION_PART.LOTTERY_ID))
        .where(Tables.TM_LOTTERY.NAME.eq(lotteryName))
        .and(Tables.TM_COMBINATION_PART.NAME.eq(combinationPartName))
        .fetchOne()

      if (tmCombinationPartId == null) {
        Future.failed(new DBException(s"Error trying to retrieve the record in TM_COMBINATION_PART with name $combinationPartName"))
      }

      tmCombinationPartId.value1()

    }
  }

  def getLotteryLastResultOf(lotteryName: String): Future[ResultRestCC] = {

    db.query { db =>

      val lastRaffleDay = db
        .select(max(TW_RESULT.RAFFLE_DAY))
        .from(TM_LOTTERY)
        .innerJoin(TM_COMBINATION_PART).on(TM_LOTTERY.ID.equal(TM_COMBINATION_PART.LOTTERY_ID))
        .innerJoin(TW_RESULT).on(TW_RESULT.COMBINATION_PART_ID.equal(TM_COMBINATION_PART.ID))
        .where(TM_LOTTERY.NAME.equal(lotteryName))
        .fetchOne().value1()


      if (lastRaffleDay == null) {
        Future.failed(new DBException(s"Error trying to retrieve the last date from TwResult with lottery name: $lotteryName"))
      }

      val combinationParts = db
        .select(TM_COMBINATION_PART.NAME, TW_RESULT.VALUES)
        .from(TM_LOTTERY)
        .innerJoin(TM_COMBINATION_PART).on(TM_LOTTERY.ID.equal(TM_COMBINATION_PART.LOTTERY_ID))
        .innerJoin(TW_RESULT).on(TW_RESULT.COMBINATION_PART_ID.equal(TM_COMBINATION_PART.ID))
        .where(TM_LOTTERY.NAME.equal(lotteryName))
        .and(TW_RESULT.RAFFLE_DAY.equal(lastRaffleDay))
        .orderBy(TM_COMBINATION_PART.PART_NUMBER)
        .fetchInto(classOf[CombinationPartRestCC])

      if (combinationParts == null) {
        Future.failed(new DBException(s"Error trying to retrieve the last result from TM_COMBINATION_PART and TW_RESULT with lottery name: $lotteryName"))
      }

      ResultRestCC(
        lastRaffleDay,
        combinationParts.toList
      )

    }
  }


}

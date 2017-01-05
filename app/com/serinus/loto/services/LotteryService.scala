package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.utils.{Constants, DB}

import scala.concurrent.Future

class LotteryService @Inject() (db: DB) {

  def getLototurfCombinationPartIdWithName(name: String) = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_LOTOTURF_NAME, name)
  }

  def getEuromillonesCombinationPartIdWithName(name: String) = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_EUROMILLONES_NAME, name)
  }

  def getGordoCombinationPartIdWithName(name: String) = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_GORDO_NAME, name)
  }

  def getBonolotoCombinationPartIdWithName(name: String) = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_BONOLOTO_NAME, name)
  }

  def getPrimitivaCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_PRIMITIVA_NAME, name)
  }

  def getCuponazoCombinationPartIdWithName(name: String): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_CUPONAZO_ONCE_NAME, name)
  }

  def getLotteryCombinationPartIdWithName(lotteryName: String, combinationPartName: String): Future[Integer] = {
    db.query { db =>

      val tmCombinationPartId = db
        .select(Tables.TM_COMBINATION_PART.ID)
        .from(Tables.TM_COMBINATION_PART)
        .innerJoin(Tables.TM_LOTTERY).on(Tables.TM_LOTTERY.ID.eq(Tables.TM_COMBINATION_PART.LOTTERY_ID))
        .where(Tables.TM_LOTTERY.NAME.eq(lotteryName))
        .and(Tables.TM_COMBINATION_PART.NAME.eq(combinationPartName))
        .fetchOne().value1()

      tmCombinationPartId

    }
  }


}

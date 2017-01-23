package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.exceptions.DBException
import com.serinus.loto.model.caseclasses.rest.{CombinationPartRestCC, LotteryRestCC, ResultRestCC}
import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.model.jooq.Tables.{TM_COMBINATION_PART, TM_LOTTERY, TW_RESULT}
import com.serinus.loto.scrapers.RaffleDate
import com.serinus.loto.types.{CombinationPartName, LotteryName}
import com.serinus.loto.utils.{Constants, DB}
import org.jooq.impl.DSL.max

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

class LotteryService @Inject()(db: DB)
                              (implicit ec: ExecutionContext) {

  def getLototurfCombinationPartIdWithName(combinationPartName: CombinationPartName): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_LOTOTURF_NAME, combinationPartName)
  }

  def getEuromillonesCombinationPartIdWithName(combinationPartName: CombinationPartName): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_EUROMILLONES_NAME, combinationPartName)
  }

  def getGordoCombinationPartIdWithName(combinationPartName: CombinationPartName): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_GORDO_NAME, combinationPartName)
  }

  def getBonolotoCombinationPartIdWithName(combinationPartName: CombinationPartName): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_BONOLOTO_NAME, combinationPartName)
  }

  def getPrimitivaCombinationPartIdWithName(combinationPartName: CombinationPartName): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_PRIMITIVA_NAME, combinationPartName)
  }

  def getCuponazoCombinationPartIdWithName(combinationPartName: CombinationPartName): Future[Integer] = {
    getLotteryCombinationPartIdWithName(Constants.TM_LOTTERY_CUPONAZO_ONCE_NAME, combinationPartName)
  }

  private def getLotteryCombinationPartIdWithName(lotteryName: LotteryName, combinationPartName: CombinationPartName): Future[Integer] = {
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

  def getLotteryLastResultOf(lotteryName: LotteryName): Future[ResultRestCC] = {

    for {

      lastRaffleDay <- getLotteryLastRaffleDayOf(lotteryName)

      resultRestCC <- getLotteryResult(lotteryName, lastRaffleDay)

    } yield {

      resultRestCC

    }

  }

  def getLotteryLastRaffleDayOf(lotteryName: LotteryName): Future[RaffleDate] = {

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

      lastRaffleDay
    }
  }


  def getLotteryResult(lotteryName: LotteryName, raffleDate: RaffleDate): Future[ResultRestCC] = {

    //TODO Add Error when no exist Result for this RaffleDate
    db.query { db =>

      val combinationParts = db
        .select(TM_COMBINATION_PART.NAME, TW_RESULT.VALUES)
        .from(TM_LOTTERY)
        .innerJoin(TM_COMBINATION_PART).on(TM_LOTTERY.ID.equal(TM_COMBINATION_PART.LOTTERY_ID))
        .innerJoin(TW_RESULT).on(TW_RESULT.COMBINATION_PART_ID.equal(TM_COMBINATION_PART.ID))
        .where(TM_LOTTERY.NAME.equal(lotteryName))
        .and(TW_RESULT.RAFFLE_DAY.equal(raffleDate))
        .orderBy(TM_COMBINATION_PART.PART_NUMBER)
        .fetchInto(classOf[CombinationPartRestCC])

      if (combinationParts == null) {
        Future.failed(new DBException(s"Error trying to retrieve the last result from TM_COMBINATION_PART and TW_RESULT with lottery name: $lotteryName"))
      }

      ResultRestCC(
        raffleDate,
        combinationParts.toList
      )

    }
  }

  def getLotteryNames: Future[Seq[LotteryRestCC]] = {

    db.query { db =>

      val lotteryNames = db.select(TM_LOTTERY.NAME)
        .from(TM_LOTTERY)
        .fetchInto(classOf[LotteryRestCC])

      lotteryNames

    }

  }

}

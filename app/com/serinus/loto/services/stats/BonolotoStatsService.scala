package com.serinus.loto.services.stats

import javax.inject.Inject

import com.serinus.loto.services.StatsService
import com.serinus.loto.utils.{Constants, DB}

import scala.concurrent.Future


class BonolotoStatsService @Inject() (db: DB,
                                      statsService: StatsService) {

  val lotteryName = Constants.TM_LOTTERY_BONOLOTO_NAME


  def findAllMainResults(): Future[Seq[String]] = {

    statsService.findAllTheResultsOfACombinationPartNameOfALottery(
      lotteryName,
      Constants.TM_COMB_PART_BONOLOTO_COMB_NAME
    )

  }

  def findAllComplementerioResults(): Future[Seq[String]] = {

    statsService.findAllTheResultsOfACombinationPartNameOfALottery(
      lotteryName,
      Constants.TM_COMB_PART_BONOLOTO_COMPL_NAME
    )

  }

  def findAllReintegroResults(): Future[Seq[String]] = {

    statsService.findAllTheResultsOfACombinationPartNameOfALottery(
      lotteryName,
      Constants.TM_COMB_PART_BONOLOTO_REINT_NAME
    )

  }

}

package com.serinus.loto.services.stats

import javax.inject.Inject

import com.serinus.loto.services.StatsService
import com.serinus.loto.utils.{Constants, DB}

import scala.concurrent.Future


class EuromillonesStatsService @Inject() (db: DB,
                                          statsService: StatsService) {

  val lotteryName = Constants.TM_LOTTERY_EUROMILLONES_NAME


  def findAllMainResults(): Future[Seq[String]] = {

    statsService.findAllTheResultsOfACombinationPartNameOfALottery(
      lotteryName,
      Constants.TM_COMB_PART_EUROMILLONES_COMB_NAME
    )

  }

}

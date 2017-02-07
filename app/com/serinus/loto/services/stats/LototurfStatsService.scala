package com.serinus.loto.services.stats

import javax.inject.Inject

import com.serinus.loto.services.StatsService
import com.serinus.loto.utils.{Constants, DB}

import scala.concurrent.Future


class LototurfStatsService @Inject() (db: DB,
                                      statsService: StatsService) {

  val lotteryName = Constants.TM_LOTTERY_LOTOTURF_NAME


  def findAllMainResults(): Future[Seq[String]] = {

    statsService.findAllTheResultsOfACombinationPartNameOfALottery(
      lotteryName,
      Constants.TM_COMB_PART_LOTOTURF_COMB_NAME
    )

  }

}

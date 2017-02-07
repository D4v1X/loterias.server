package com.serinus.loto.services.lotteries

import java.time.LocalDate
import javax.inject.Inject

import com.serinus.loto.model.caseclasses.ResultCC
import com.serinus.loto.{CombinationPartName, RaffleDate}
import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.{Constants, DB}

import scala.concurrent.Future


class EuromillonesLotteryService @Inject() (db: DB,
                                            lotteryService: LotteryService) {

  val lotteryName = Constants.TM_LOTTERY_EUROMILLONES_NAME


  def findCombinationPartIdWithName(combinationPartName: CombinationPartName): Future[Integer] = {
    lotteryService.findLotteryCombinationPartIdWithName(lotteryName, combinationPartName)
  }

  def findLastResult(): Future[ResultCC] = {
    lotteryService.findLotteryLastResultOf(lotteryName)
  }

  def findResultForDate(raffleDate: RaffleDate): Future[ResultCC] = {
    lotteryService.findLotteryResult(lotteryName, raffleDate)
  }

}

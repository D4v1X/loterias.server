package com.serinus.loto.lotostats

import java.time.LocalDate
import javax.inject.Inject

import com.serinus.loto.RaffleDate
import com.serinus.loto.model.caseclasses.ResultCC
import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants

import scala.concurrent.Future

class EuromillonesStats @Inject() (lotteryService: LotteryService) {

  def findLastResult(): Future[ResultCC] = {
    lotteryService.getLotteryLastResultOf(Constants.TM_LOTTERY_EUROMILLONES_NAME)
  }


  def findResultForDate(date: String): Future[ResultCC] = {
    //TODO Validation Date or Binding
    val raffleDate: RaffleDate = LocalDate.parse(date)
    lotteryService.getLotteryResult(Constants.TM_LOTTERY_EUROMILLONES_NAME, raffleDate)
  }

}

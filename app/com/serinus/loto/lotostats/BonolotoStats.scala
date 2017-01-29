package com.serinus.loto.lotostats

import javax.inject.Inject

import com.serinus.loto.model.caseclasses.ResultCC
import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants

import scala.concurrent.Future

class BonolotoStats @Inject() (lotteryService: LotteryService) {

  def findLastResult(): Future[ResultCC] = {
    lotteryService.getLotteryLastResultOf(Constants.TM_LOTTERY_BONOLOTO_NAME)
  }

}

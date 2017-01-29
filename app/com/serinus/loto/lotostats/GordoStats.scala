package com.serinus.loto.lotostats

import javax.inject.Inject

import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants

class GordoStats @Inject() (lotteryService: LotteryService) {

  def findLastResult() = {
    lotteryService.getLotteryLastResultOf(Constants.TM_LOTTERY_GORDO_NAME)
  }

}

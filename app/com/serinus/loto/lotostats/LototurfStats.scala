package com.serinus.loto.lotostats

import javax.inject.Inject

import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants

class LototurfStats @Inject() (lotteryService: LotteryService) {

  def findLastResult() = {
    lotteryService.getLotteryLastResultOf(Constants.TM_LOTTERY_LOTOTURF_NAME)
  }

}

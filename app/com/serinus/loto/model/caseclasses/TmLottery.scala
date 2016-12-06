package com.serinus.loto.model.caseclasses

import com.serinus.loto.model.pojos.TmLottery


case class TmLotteryCC(id : Int,
                       name : String)


object TmLotteryCC {

  def fromPojo(tmLottery: TmLottery): TmLotteryCC = {

    TmLotteryCC(
      tmLottery.getId,
      tmLottery.getName
    )

  }

}

package com.serinus.loto.model.caseclasses

import com.serinus.loto.LotteryName
import com.serinus.loto.model.pojos.TmLottery

import scala.collection.mutable.ListBuffer


final case class TmLotteryCC(id: Int, name: String)
final case class LotteryCC(name: LotteryName)


object TmLotteryCC {


  def fromPojoList(tmLotteryList: Seq[TmLottery]): List[TmLotteryCC] = {

    val tmLotteryCCList = ListBuffer[TmLotteryCC]()

    tmLotteryList foreach { tmLottery =>
      tmLotteryCCList += fromPojo(tmLottery)
    }

    tmLotteryCCList.toList

  }

  def fromPojo(tmLottery: TmLottery): TmLotteryCC = {

    TmLotteryCC(
      tmLottery.getId,
      tmLottery.getName
    )

  }

}

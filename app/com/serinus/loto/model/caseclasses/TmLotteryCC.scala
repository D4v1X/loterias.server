package com.serinus.loto.model.caseclasses

import java.util

import com.serinus.loto.model.pojos.TmLottery

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ListBuffer

case class TmLotteryCC(id: Int,
                       name: String)


object TmLotteryCC {

  def fromPojoList(tmLotteryList: util.List[TmLottery]): List[TmLotteryCC] = {

    val tmLotteryCCList = ListBuffer[TmLotteryCC]()
    asScalaBuffer(tmLotteryList) foreach (tmLottery => tmLotteryCCList += fromPojo(tmLottery))
    tmLotteryCCList.toList

  }

  def fromPojo(tmLottery: TmLottery): TmLotteryCC = {

    TmLotteryCC(
      tmLottery.getId,
      tmLottery.getName
    )

  }

}

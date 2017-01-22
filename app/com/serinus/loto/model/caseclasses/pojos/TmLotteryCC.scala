package com.serinus.loto.model.caseclasses.pojos

import com.serinus.loto.model.pojos.TmLottery
import play.api.libs.json.{Json, OWrites}

import scala.collection.mutable.ListBuffer

case class TmLotteryCC(id: Int,
                       name: String)


object TmLotteryCC {

  implicit val tmLotteryWrites: OWrites[TmLotteryCC] = Json.writes[TmLotteryCC]

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

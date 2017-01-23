package com.serinus.loto.model.caseclasses.rest

import com.serinus.loto.types.LotteryName
import play.api.libs.json.{Json, OWrites}


case class LotteryRestCC(name: LotteryName)

object LotteryRestCC {

  implicit val lotteryRestWrites: OWrites[LotteryRestCC] = Json.writes[LotteryRestCC]

}

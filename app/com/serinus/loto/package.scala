package com.serinus

import java.time.LocalDate

import com.serinus.loto.model.caseclasses._
import play.api.libs.json.Json

package object loto {

  type RaffleDate = LocalDate
  type CombinationPartId = Int
  type ResultValue = String
  type ScrapResult = (RaffleDate, CombinationPartId, ResultValue)
  type ScrapResultList = Seq[ScrapResult]
  type ScrapError = String
  type StatsError = String
  type LotteryName = String
  type CombinationPartName = String



  case class Freq(number: Int, frequency: Int)
  type FreqMap = Seq[Freq]



  implicit val tmCombinationWrites = Json.writes[TmCombinationPartCC]
  implicit val combinationPartWrites = Json.writes[CombinationPartCC]
  implicit val twResultWrites = Json.writes[TwResultCC]
  implicit val resultWrites = Json.writes[ResultCC]
  implicit val tmLotteryWrites = Json.writes[TmLotteryCC]
  implicit val lotteryWrites = Json.writes[LotteryCC]
  implicit val freqWrites = Json.writes[Freq]



}

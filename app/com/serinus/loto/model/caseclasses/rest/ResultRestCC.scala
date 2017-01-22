package com.serinus.loto.model.caseclasses.rest

import java.time.LocalDate

import play.api.libs.json.{Json, OWrites}

case class ResultRestCC(raffleDay: LocalDate,
                        combinationParts: List[CombinationPartRestCC])

object ResultRestCC {

  implicit val resultRestWrites: OWrites[ResultRestCC] = Json.writes[ResultRestCC]

}

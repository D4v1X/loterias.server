package com.serinus.loto.model.caseclasses.rest

import play.api.libs.json.{Json, OWrites}

case class CombinationPartRestCC(partName: String,
                                 partValue: String)

object CombinationPartRestCC {

  implicit val combinationPartRestWrites: OWrites[CombinationPartRestCC] = Json.writes[CombinationPartRestCC]

}
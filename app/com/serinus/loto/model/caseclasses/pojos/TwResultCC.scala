package com.serinus.loto.model.caseclasses.pojos

import java.time.LocalDate

import com.serinus.loto.model.pojos.TwResult
import play.api.libs.json.{Json, OWrites}

import scala.collection.mutable.ListBuffer


case class TwResultCC(id: Int,
                      raffleDay: LocalDate,
                      combinationPartId: Int,
                      values: String)


object TwResultCC {

  implicit val twResultWrites: OWrites[TwResultCC] = Json.writes[TwResultCC]

  def fromPojoList(twResultList: Seq[TwResult]): List[TwResultCC] = {

    val twResultCCList = ListBuffer[TwResultCC]()

    twResultList foreach { twResult =>
      twResultCCList += fromPojo(twResult)
    }

    twResultCCList.toList

  }

  def fromPojo(result: TwResult): TwResultCC = {

    TwResultCC(
      result.getId,
      result.getRaffleDay,
      result.getCombinationPartId,
      result.getValues
    )

  }

}

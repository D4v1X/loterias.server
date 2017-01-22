package com.serinus.loto.model.caseclasses.pojos

import com.serinus.loto.model.pojos.TmCombinationPart
import play.api.libs.json.{Json, OWrites}

import scala.collection.mutable.ListBuffer


case class TmCombinationPartCC(id: Int,
                               lotteryId: Int,
                               partNumber: Short,
                               name: String,
                               numValues: Short)


object TmCombinationPartCC {

  implicit val tmCombinationWrites: OWrites[TmCombinationPartCC] = Json.writes[TmCombinationPartCC]

  def fromPojoList(tmCombinationList: Seq[TmCombinationPart]): List[TmCombinationPartCC] = {

    val tmCombinationCCList = ListBuffer[TmCombinationPartCC]()

    tmCombinationList foreach { tmCombinationPart =>
      tmCombinationCCList += fromPojo(tmCombinationPart)
    }

    tmCombinationCCList.toList

  }

  def fromPojo(combinationPart: TmCombinationPart): TmCombinationPartCC = {

    TmCombinationPartCC(
      combinationPart.getId,
      combinationPart.getLotteryId,
      combinationPart.getPartNumber,
      combinationPart.getName,
      combinationPart.getNumValues
    )

  }

}

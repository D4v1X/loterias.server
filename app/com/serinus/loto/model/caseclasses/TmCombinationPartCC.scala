package com.serinus.loto.model.caseclasses

import com.serinus.loto.model.pojos.TmCombinationPart

import scala.collection.mutable.ListBuffer


final case class TmCombinationPartCC(id: Int,
                                     lotteryId: Int,
                                     partNumber: Short,
                                     name: String,
                                     numValues: Short)
final case class CombinationPartCC(partName: String,
                                   partValue: String)


object TmCombinationPartCC {


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

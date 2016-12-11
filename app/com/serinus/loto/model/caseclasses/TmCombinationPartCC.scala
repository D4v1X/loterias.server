package com.serinus.loto.model.caseclasses

import java.util

import com.serinus.loto.model.pojos.TmCombinationPart

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ListBuffer


case class TmCombinationPartCC(id : Int,
                               lotteryId : Int,
                               partNumber : Short,
                               name : String,
                               numValues : Short)


object TmCombinationPartCC {

  def fromPojoList(tmCombinationList: util.List[TmCombinationPart]): List[TmCombinationPartCC] = {

    val tmCombinationCCList = ListBuffer[TmCombinationPartCC]()
    asScalaBuffer(tmCombinationList) foreach (tmCombinationPart => tmCombinationCCList += fromPojo(tmCombinationPart))
    tmCombinationCCList.toList

  }

  def fromPojo(combinationPart : TmCombinationPart): TmCombinationPartCC = {

    TmCombinationPartCC(
      combinationPart.getId,
      combinationPart.getLotteryId,
      combinationPart.getPartNumber,
      combinationPart.getName,
      combinationPart.getNumValues
    )

  }

}

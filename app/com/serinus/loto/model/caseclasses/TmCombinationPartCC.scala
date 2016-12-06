package com.serinus.loto.model.caseclasses

import com.serinus.loto.model.pojos.TmCombinationPart


case class TmCombinationPartCC(id : Int,
                               lotteryId : Int,
                               partNumber : Short,
                               name : String,
                               numValues : Short)


object TmCombinationPartCC {

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

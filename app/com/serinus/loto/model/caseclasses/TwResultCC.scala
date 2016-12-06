package com.serinus.loto.model.caseclasses

import java.time.LocalDate

import com.serinus.loto.model.pojos.TwResult


case class TwResultCC(id : Int,
                      raffleDay : LocalDate,
                      combinationPartId : Int,
                      values : String)


object TwResultCC {

  def fromPojo(result: TwResult): TwResultCC = {

    TwResultCC(
      result.getId,
      result.getRaffleDay,
      result.getCombinationPartId,
      result.getValues
    )

  }

}

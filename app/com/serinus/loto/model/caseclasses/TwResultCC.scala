package com.serinus.loto.model.caseclasses

import java.time.LocalDate

import com.serinus.loto.model.pojos.TwResult

import scala.collection.mutable.ListBuffer


final case class TwResultCC(id: Int,
                            raffleDay: LocalDate,
                            combinationPartId: Int,
                            values: String)
final case class ResultCC(raffleDay: LocalDate,
                          combinationParts: List[CombinationPartCC])


object TwResultCC {

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

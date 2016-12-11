package com.serinus.loto.model.caseclasses

import java.time.LocalDate
import java.util

import com.serinus.loto.model.pojos.TwResult

import scala.collection.JavaConversions.asScalaBuffer
import scala.collection.mutable.ListBuffer


case class TwResultCC(id : Int,
                      raffleDay : LocalDate,
                      combinationPartId : Int,
                      values : String)


object TwResultCC {

  def fromPojoList(twResultList: util.List[TwResult]): List[TwResultCC] = {

    val twResultCCList = ListBuffer[TwResultCC]()
    asScalaBuffer(twResultList) foreach (twResult => twResultCCList += fromPojo(twResult))
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

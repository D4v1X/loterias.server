package com.serinus.loto

import java.time.LocalDate

package object scrapers {

  type RaffleDate = LocalDate
  type CombinationPartId = Int
  type ResultValue = String
  type ScrapResult = (RaffleDate, CombinationPartId, ResultValue)
  type ScrapResultList = Seq[ScrapResult]
  type ScrapError = String

}

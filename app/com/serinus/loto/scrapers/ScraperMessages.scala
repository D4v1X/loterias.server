package com.serinus.loto.scrapers

import java.time.LocalDate

object ScraperMessages {

  sealed trait ActorMessage
  case object ScrapCuponazo extends ActorMessage
  case class ScrapHistoricCuponazo(initialDate: Option[LocalDate], finalDate: Option[LocalDate])
  case object ScrapEuromillones extends ActorMessage
  case class ScrapHistoricEuromillones(initialDate: Option[LocalDate], finalDate: Option[LocalDate])
  case object ScrapPrimitiva extends ActorMessage
  case object ScrapBonoloto extends ActorMessage
  case object ScrapGordo extends ActorMessage
  case object ScrapLototurf extends ActorMessage

}

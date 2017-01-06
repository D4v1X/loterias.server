package com.serinus.loto.scrapers

import java.time.LocalDate

object ScraperMessages {

  sealed trait ActorMessage
  case object ScrapCuponazo extends ActorMessage
  case class ScrapHistoricCuponazo(initialDate: Option[LocalDate], finalDate: Option[LocalDate]) extends ActorMessage
  case object ScrapEuromillones extends ActorMessage
  case class ScrapHistoricEuromillones(initialDate: Option[LocalDate], finalDate: Option[LocalDate]) extends ActorMessage
  case object ScrapPrimitiva extends ActorMessage
  case class ScrapHistoricPrimitiva(initialDate: Option[LocalDate], finalDate: Option[LocalDate]) extends ActorMessage
  case object ScrapBonoloto extends ActorMessage
  case class ScrapHistoricBonoloto(initialDate: Option[LocalDate], finalDate: Option[LocalDate]) extends ActorMessage
  case object ScrapGordo extends ActorMessage
  case class ScrapHistoricGordo(initialDate: Option[LocalDate], finalDate: Option[LocalDate]) extends ActorMessage
  case object ScrapLototurf extends ActorMessage
  case class ScrapHistoricLototurf(initialDate: Option[LocalDate], finalDate: Option[LocalDate]) extends ActorMessage

}

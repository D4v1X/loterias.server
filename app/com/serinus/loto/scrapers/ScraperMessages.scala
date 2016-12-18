package com.serinus.loto.scrapers

object ScraperMessages {

  sealed trait ActorMessage
  case object ScrapCuponazo extends ActorMessage
  case object ScrapPrimitiva extends ActorMessage
  case object ScrapBonoloto extends ActorMessage


}

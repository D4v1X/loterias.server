package com.serinus.loto.utils

import javax.inject.{Inject, Named}

import akka.actor.{ActorRef, ActorSystem}
import com.serinus.loto.scrapers.ScraperMessages
import com.typesafe.akka.extension.quartz.QuartzSchedulerExtension

class JobScheduler @Inject()
  (system: ActorSystem)
  (@Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME) cuponazoOnceScaper: ActorRef)
  (@Named(Constants.EUROMILLONES_SCRAPER_NAME) euromillonesScaper: ActorRef)
  (@Named(Constants.PRIMITIVA_SCRAPER_NAME) primitivaScaper: ActorRef)
  (@Named(Constants.BONOLOTO_SCRAPER_NAME) bonolotoScraper: ActorRef)
  (@Named(Constants.GORDO_SCRAPER_NAME) gordoScraper: ActorRef)
  (@Named(Constants.LOTOTURF_SCRAPER_NAME) lototurfScraper: ActorRef) {


  // At long last! a scheduler using CRON expressions
  val scheduler = QuartzSchedulerExtension(system)


  // declare here any scrapers with their scheduled expression name and the message to send to them
  scheduler.schedule("CuponazoScrapSchedule", cuponazoOnceScaper, ScraperMessages.ScrapCuponazo)
  scheduler.schedule("EuromillonesScrapSchedule", euromillonesScaper, ScraperMessages.ScrapEuromillones)
  scheduler.schedule("PrimitivaScrapSchedule", primitivaScaper, ScraperMessages.ScrapPrimitiva)
  scheduler.schedule("BonolotoScrapSchedule", bonolotoScraper, ScraperMessages.ScrapBonoloto)
  scheduler.schedule("GordoScrapSchedule", bonolotoScraper, ScraperMessages.ScrapBonoloto)
  scheduler.schedule("LototurfScrapSchedule", lototurfScraper, ScraperMessages.ScrapLototurf)

}

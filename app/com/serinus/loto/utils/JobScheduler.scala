package com.serinus.loto.utils

import javax.inject.{Inject, Named}

import akka.actor.{ActorRef, ActorSystem}

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

class JobScheduler @Inject() (system: ActorSystem, db: DB)
  (@Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME) cuponazoOnceScaper: ActorRef)
  (implicit ec: ExecutionContext) {


  // execute Cuponazo scraper every day at midnight
  system.scheduler.schedule(
    Duration(0, MILLISECONDS),
    Duration(24, HOURS),
    cuponazoOnceScaper,
    Constants.SCHEDULER_MSG_SCRAP_CUPONAZO_ONCE
  )


}

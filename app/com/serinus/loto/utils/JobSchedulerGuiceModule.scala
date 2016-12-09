package com.serinus.loto.utils

import com.google.inject.AbstractModule
import com.serinus.loto.scrapers.CuponazoOnceScraper
import play.api.libs.concurrent.AkkaGuiceSupport

class JobSchedulerGuiceModule extends AbstractModule with AkkaGuiceSupport {

  def configure() = {

    bindActor[CuponazoOnceScraper](Constants.CUPONAZO_ONCE_SCRAPER_NAME)

    bind(classOf[JobScheduler]).asEagerSingleton()

  }

}

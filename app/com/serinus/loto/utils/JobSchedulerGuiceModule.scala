package com.serinus.loto.utils

import com.google.inject.AbstractModule
import com.serinus.loto.scrapers.CuponazoOnceScraper
import play.api.libs.concurrent.AkkaGuiceSupport

class JobSchedulerGuiceModule extends AbstractModule with AkkaGuiceSupport {

  def configure() = {

    // declare here any scraper actor so that it can be injectable into other modules via Guice
    bindActor[CuponazoOnceScraper](Constants.CUPONAZO_ONCE_SCRAPER_NAME)

    // Make JobScheduler an eager singleton so that is starts when the application does
    bind(classOf[JobScheduler]).asEagerSingleton()

  }

}

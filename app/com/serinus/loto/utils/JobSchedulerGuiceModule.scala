package com.serinus.loto.utils

import com.google.inject.AbstractModule
import com.serinus.loto.scrapers._
import play.api.libs.concurrent.AkkaGuiceSupport

class JobSchedulerGuiceModule extends AbstractModule with AkkaGuiceSupport {

  def configure() = {

    // declare here any scraper actor so that it can be injectable into other modules via Guice
    bindActor[CuponazoOnceScraper](Constants.CUPONAZO_ONCE_SCRAPER_NAME)
    bindActor[EuromillonesScraper](Constants.EUROMILLONES_SCRAPER_NAME)
    bindActor[PrimitivaScraper](Constants.PRIMITIVA_SCRAPER_NAME)
    bindActor[BonolotoScraper](Constants.BONOLOTO_SCRAPER_NAME)
    bindActor[GordoScraper](Constants.GORDO_SCRAPER_NAME)
    bindActor[LototurfScraper](Constants.LOTOTURF_SCRAPER_NAME)

    // Make JobScheduler an eager singleton so that is starts when the application does
    bind(classOf[JobScheduler]).asEagerSingleton()

  }

}

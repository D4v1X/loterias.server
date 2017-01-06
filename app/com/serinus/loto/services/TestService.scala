package com.serinus.loto.services

import java.time.LocalDate
import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import com.serinus.loto.model.jooq.Tables._
import com.serinus.loto.model.pojos._
import com.serinus.loto.scrapers.ScraperMessages
import com.serinus.loto.utils.{Constants, DB}

class TestService @Inject()(db: DB,
                            @Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME) cuponazoOnceScaper: ActorRef,
                            @Named(Constants.EUROMILLONES_SCRAPER_NAME) euromillonesScraper: ActorRef,
                            @Named(Constants.PRIMITIVA_SCRAPER_NAME) primitivaScraper: ActorRef,
                            @Named(Constants.BONOLOTO_SCRAPER_NAME) bonolotoScraper: ActorRef,
                            @Named(Constants.GORDO_SCRAPER_NAME) gordoScraper: ActorRef,
                            @Named(Constants.LOTOTURF_SCRAPER_NAME) lototurfScraper: ActorRef) {


  def hello = {
    db.query { db =>

      db.selectFrom(TM_LOTTERY).fetchOneInto(classOf[TmLottery])

    }
  }


  def testCuponazoParser = {
    cuponazoOnceScaper ! ScraperMessages.ScrapCuponazo
  }


  def testHistoricCuponazoParser = {
    cuponazoOnceScaper ! ScraperMessages.ScrapHistoricCuponazo(Some(LocalDate.of(2012, 6, 8)), None)
  }


  def testEuromillonesParser = {
    euromillonesScraper ! ScraperMessages.ScrapEuromillones
  }

  def testHistoricEuromillonesParser = {
    euromillonesScraper ! ScraperMessages.ScrapHistoricEuromillones(Some(LocalDate.of(2004, 5, 14)), None)
  }


  def testPrimitivaParser = {
    primitivaScraper ! ScraperMessages.ScrapPrimitiva
  }

  def testHistoricPrimitivaParser = {
    primitivaScraper ! ScraperMessages.ScrapHistoricPrimitiva(Some(LocalDate.of(2004, 5, 13)), None)
  }


  def testBonolotoParser = {
    bonolotoScraper ! ScraperMessages.ScrapBonoloto
  }

  def testHistoricBonolotoParser = {
    bonolotoScraper ! ScraperMessages.ScrapHistoricBonoloto(Some(LocalDate.of(2004, 5, 18)), None)
  }


  def testGordoParser = {
    gordoScraper ! ScraperMessages.ScrapGordo
  }

  def testHistoricGordoParser = {
    gordoScraper ! ScraperMessages.ScrapHistoricGordo(Some(LocalDate.of(2004, 5, 16)), None)
  }


  def testLototurfParser = {
    lototurfScraper ! ScraperMessages.ScrapLototurf
  }

  def testHistoricLototurfParser = {
    lototurfScraper ! ScraperMessages.ScrapHistoricLototurf(Some(LocalDate.of(2006, 4, 16)), None)
  }


  def scrapAllHistoricData = {
    cuponazoOnceScaper ! ScraperMessages.ScrapHistoricCuponazo(Some(LocalDate.of(2012, 6, 8)), None)
    euromillonesScraper ! ScraperMessages.ScrapHistoricEuromillones(Some(LocalDate.of(2004, 5, 14)), None)
    primitivaScraper ! ScraperMessages.ScrapHistoricPrimitiva(Some(LocalDate.of(2004, 5, 13)), None)
    bonolotoScraper ! ScraperMessages.ScrapHistoricBonoloto(Some(LocalDate.of(2004, 5, 18)), None)
    gordoScraper ! ScraperMessages.ScrapHistoricGordo(Some(LocalDate.of(2004, 5, 16)), None)
    lototurfScraper ! ScraperMessages.ScrapHistoricLototurf(Some(LocalDate.of(2006, 4, 16)), None)
  }



  def testLottery = {
    db.query { db =>

      db.selectFrom(TM_LOTTERY).fetchInto(classOf[TmLottery])

    }
  }


  def testCombination = {
    db.query { db =>

      db.selectFrom(TM_COMBINATION_PART).fetchInto(classOf[TmCombinationPart])

    }
  }


  def testResult = {
    db.query { db =>

      db.selectFrom(TW_RESULT).fetchInto(classOf[TwResult])

    }
  }

}

package com.serinus.loto.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import com.serinus.loto.model.jooq.Tables._
import com.serinus.loto.model.pojos._
import com.serinus.loto.utils.Constants
import com.serinus.loto.scrapers.{PrimitivaScraper, ScraperMessages}
import com.serinus.loto.utils.DB

class TestService @Inject()(db: DB,
                            @Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME) cuponazoOnceScaper: ActorRef,
                            @Named(Constants.PRIMITIVA_SCRAPER_NAME) primitivaScraper: ActorRef,
                            @Named(Constants.BONOLOTO_SCRAPER_NAME) bonolotoScraper: ActorRef) {


  def hello = {
    db.query { db =>

      db.selectFrom(TM_LOTTERY).fetchOneInto(classOf[TmLottery])

    }
  }


  def testCuponazoParser = {
    cuponazoOnceScaper ! ScraperMessages.ScrapCuponazo
  }


  def testPrimitivaParser = {
    primitivaScraper ! ScraperMessages.ScrapPrimitiva
  }


  def testBonolotoParser = {
    bonolotoScraper ! ScraperMessages.ScrapBonoloto
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

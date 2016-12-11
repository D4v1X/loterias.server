package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables._
import com.serinus.loto.model.pojos._
import com.serinus.loto.scrapers.{CuponazoOnceScraper, PrimitivaScraper}
import com.serinus.loto.utils.DB

class TestService @Inject() (db: DB,
                             cuponazoOnceScraper: CuponazoOnceScraper,
                             primitivaScraper: PrimitivaScraper) {


  def hello = {
    db.query { db =>

      db.selectFrom(TM_LOTTERY).fetchOneInto(classOf[TmLottery])

    }
  }

  def testCuponazoParser = {
    cuponazoOnceScraper.run
  }

  def testPrimitivaParser = {
    primitivaScraper.run
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

package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables.{TM_COMBINATION_PART, TM_LOTTERY, TW_RESULT}
import com.serinus.loto.model.pojos.{TmCombinationPart, TmLottery, TwResult}
import com.serinus.loto.utils.DB


class TestService @Inject()(db: DB) {


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

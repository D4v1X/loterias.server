package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables._
import com.serinus.loto.model.pojos._
import com.serinus.loto.utils.DB

class TestService @Inject() (db: DB) {


  def hello = {
    db.query { db =>

      db.selectFrom(TM_LOTTERY).fetchOneInto(classOf[TmLottery])

    }
  }


}

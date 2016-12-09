package com.serinus.loto.services

import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import com.serinus.loto.model.jooq.Tables._
import com.serinus.loto.model.pojos._
import com.serinus.loto.utils.{Constants, DB}

class TestService @Inject()
  (db: DB)
  (@Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME) cuponazoOnceScaper: ActorRef) {


  def hello = {
    db.query { db =>

      db.selectFrom(TM_LOTTERY).fetchOneInto(classOf[TmLottery])

    }
  }

  def testCuponazoParser = {
    cuponazoOnceScaper ! Constants.SCHEDULER_MSG_SCRAP_CUPONAZO_ONCE
  }


}

package com.serinus.loto.services

import javax.inject.Inject

import com.serinus.loto.model.jooq.Tables
import com.serinus.loto.utils.{Constants, DB}

import scala.concurrent.Future

class LotteryService @Inject() (db: DB) {


  def getCuponazoCombinationPartIdWithName(name: String): Future[Integer] = {
    db.query { db =>

      val tmCombinationPartId = db
        .select(Tables.TM_COMBINATION_PART.ID)
        .from(Tables.TM_COMBINATION_PART)
        .innerJoin(Tables.TM_LOTTERY).on(Tables.TM_LOTTERY.ID.eq(Tables.TM_COMBINATION_PART.LOTTERY_ID))
        .where(Tables.TM_LOTTERY.NAME.eq(Constants.TM_LOTTERY_CUPONAZO_ONCE_NAME))
        .and(Tables.TM_COMBINATION_PART.NAME.eq(name))
        .fetchOne().value1()

      tmCombinationPartId

    }
  }


}

package com.serinus.loto.controllers

import java.time.LocalDate
import javax.inject.Inject

import com.serinus.loto.scrapers.RaffleDate
import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext


class EuromillonesController @Inject()(lotteryServer: LotteryService)
                                      (implicit ec: ExecutionContext)
  extends Controller {


  def lastResult: Action[AnyContent] = Action.async {

    lotteryServer.getLotteryLastResultOf(Constants.TM_LOTTERY_EUROMILLONES_NAME).map { resultRestCC =>

      Ok(Json.toJson(resultRestCC))

    }

  }

  def result(date: String): Action[AnyContent] = Action.async {

    //TODO Validation Date or Binding
    val raffleDate: RaffleDate = LocalDate.parse(date)

    lotteryServer.getLotteryResult(Constants.TM_LOTTERY_EUROMILLONES_NAME, raffleDate).map { resultRestCC =>

      Ok(Json.toJson(resultRestCC))

    }

  }

}

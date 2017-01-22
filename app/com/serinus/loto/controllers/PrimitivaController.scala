package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext


class PrimitivaController @Inject()(lotteryServer: LotteryService)
                                   (implicit ec: ExecutionContext)
  extends Controller {


  def lastResult: Action[AnyContent] = Action.async {

    lotteryServer.getLotteryLastResultOf(Constants.TM_LOTTERY_PRIMITIVA_NAME).map { resultRestCC =>

      Ok(Json.toJson(resultRestCC))

    }

  }

}

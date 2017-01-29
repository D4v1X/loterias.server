package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class LototurfController @Inject()(lotteryServer: LotteryService) extends Controller {


  def lastResult: Action[AnyContent] = Action.async {

    lotteryServer.getLotteryLastResultOf(Constants.TM_LOTTERY_LOTOTURF_NAME).map { resultRestCC =>

      Ok(Json.toJson(resultRestCC))

    }

  }

}

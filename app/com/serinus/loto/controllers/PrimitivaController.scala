package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.PrimitivaStats
import com.serinus.loto.services.LotteryService
import com.serinus.loto.utils.Constants
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


class PrimitivaController @Inject() (primitivaStats: PrimitivaStats,
                                     lotteryService: LotteryService) extends Controller {

  def lastResult: Action[AnyContent] = Action.async {

    lotteryService.getLotteryLastResultOf(Constants.TM_LOTTERY_PRIMITIVA_NAME).map { resultRestCC =>

      Ok(Json.toJson(resultRestCC))

    }

  }

  def computeFrequencies = Action.async {

    primitivaStats.computeFrequencies() map {
      case Right(freqs) => Ok(Json.toJson(freqs))
      case Left(err) => InternalServerError(s"Error computing primitiva freqs $err")
    }

  }

}



package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.LototurfStats
import com.serinus.loto.services.lotteries.LototurfLotteryService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class LototurfController @Inject() (lototurfStats: LototurfStats,
                                    lototurfLotteryService: LototurfLotteryService) extends Controller {

  def findLastResult = Action.async {
    lototurfLotteryService.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Lototurf result: ${err.getMessage}")
    }
  }

}

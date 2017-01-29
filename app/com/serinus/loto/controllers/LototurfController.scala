package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.LototurfStats
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class LototurfController @Inject() (lototurfStats: LototurfStats) extends Controller {

  def findLastResult = Action.async {
    lototurfStats.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Lototurf result: ${err.getMessage}")
    }
  }

}

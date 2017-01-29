package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.CuponazoStats
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class CuponazoOnceController @Inject() (cuponazoStats: CuponazoStats) extends Controller {

  def findLastResult = Action.async {
    cuponazoStats.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Cuponazo result: ${err.getMessage}")
    }
  }

}

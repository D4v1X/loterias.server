package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.GordoStats
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class GordoController @Inject() (gordoStats: GordoStats) extends Controller {

  def findLastResult = Action.async {
    gordoStats.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Gordo result: ${err.getMessage}")
    }
  }

}

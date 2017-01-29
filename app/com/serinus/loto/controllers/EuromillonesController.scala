package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.EuromillonesStats
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class EuromillonesController @Inject() (euromillonesStats: EuromillonesStats) extends Controller {

  def findLastResult = Action.async {
    euromillonesStats.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Euromillones result: ${err.getMessage}")
    }
  }


  def findResultForDate(date: String) = Action.async {
    euromillonesStats.findResultForDate(date) map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the Euromillones result for date $date: ${err.getMessage}")
    }
  }

}

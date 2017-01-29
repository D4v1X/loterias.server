package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.PrimitivaStats
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global


class PrimitivaController @Inject() (primitivaStats: PrimitivaStats) extends Controller {


  def findLastResult = Action.async {
    primitivaStats.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Primitiva result, ${err.getMessage}")
    }
  }


  def findFrequencies = Action.async {
    primitivaStats.computeFrequencies() map {
      case Right(freqs) => Ok(Json.toJson(freqs))
      case Left(err) => InternalServerError(s"Error computing primitiva freqs $err")
    }
  }

}



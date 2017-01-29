package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.BonolotoStats
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class BonolotoController @Inject() (bonolotoStats: BonolotoStats) extends Controller {

  def findLastResult = Action.async {
    bonolotoStats.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Bonoloto result: ${err.getMessage}")
    }
  }

}

package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.GordoStats
import com.serinus.loto.services.lotteries.GordoLotteryService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class GordoController @Inject() (gordoStats: GordoStats,
                                 gordoLotteryService: GordoLotteryService) extends Controller {

  def findLastResult = Action.async {
    gordoLotteryService.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Gordo result: ${err.getMessage}")
    }
  }

  def findFrequencies = Action.async {
    gordoStats.computeFrequencies() map {
      case Right(freqs) => Ok(Json.toJson(freqs))
      case Left(err) => InternalServerError(s"Error computing the Gordo frequencies: $err")
    }
  }


  def findMostFrequentCombination = Action.async {
    gordoStats.computeMostFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the most frequent Gordo combination: $err")
    }

  }


  def findLeastFrequentCombination = Action.async {
    gordoStats.computeLeastFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the least frequent Gordo combination: $err")
    }

  }

}

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

  def findFrequencies = Action.async {
    lototurfStats.computeFrequencies() map {
      case Right(freqs) => Ok(Json.toJson(freqs))
      case Left(err) => InternalServerError(s"Error computing the Lototurf frequencies: $err")
    }
  }


  def findMostFrequentCombination = Action.async {
    lototurfStats.computeMostFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the most frequent Lototurf combination: $err")
    }

  }


  def findLeastFrequentCombination = Action.async {
    lototurfStats.computeLeastFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the least frequent Lototurf combination: $err")
    }

  }

}

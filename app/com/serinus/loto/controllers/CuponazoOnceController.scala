package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.CuponazoOnceStats
import com.serinus.loto.services.lotteries.CuponazoOnceLotteryService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class CuponazoOnceController @Inject() (cuponazoStats: CuponazoOnceStats,
                                        cuponazoOnceLotteryService: CuponazoOnceLotteryService) extends Controller {

  def findLastResult = Action.async {
    cuponazoOnceLotteryService.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Cuponazo result: ${err.getMessage}")
    }
  }

  def findFrequencies = Action.async {
    cuponazoStats.computeFrequencies() map {
      case Right(freqs) => Ok(Json.toJson(freqs))
      case Left(err) => InternalServerError(s"Error computing the Cuponazo frequencies: $err")
    }
  }


  def findMostFrequentCombination = Action.async {
    cuponazoStats.computeMostFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the most frequent Cuponazo combination: $err")
    }

  }


  def findLeastFrequentCombination = Action.async {
    cuponazoStats.computeLeastFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the least frequent Cuponazo combination: $err")
    }

  }

}

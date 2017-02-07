package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.lotostats.BonolotoStats
import com.serinus.loto.services.lotteries.BonolotoLotteryService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class BonolotoController @Inject() (bonolotoStats: BonolotoStats,
                                    bonolotoLotteryService: BonolotoLotteryService) extends Controller {

  def findLastResult = Action.async {
    bonolotoLotteryService.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Bonoloto result: ${err.getMessage}")
    }
  }

  def findFrequencies = Action.async {
    bonolotoStats.computeFrequencies() map {
      case Right(freqs) => Ok(Json.toJson(freqs))
      case Left(err) => InternalServerError(s"Error computing the Bonoloto frequencies: $err")
    }
  }


  def findMostFrequentCombination = Action.async {
    bonolotoStats.computeMostFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the most frequent Bonoloto combination: $err")
    }

  }


  def findLeastFrequentCombination = Action.async {
    bonolotoStats.computeLeastFrequentCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the least frequent Bonoloto combination: $err")
    }

  }

}

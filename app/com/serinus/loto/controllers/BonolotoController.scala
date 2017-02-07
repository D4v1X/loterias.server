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


  def findFrequenciesMainCombination = Action.async {
    bonolotoStats.computeFrequenciesMainCombination() map {
      case Right(freqs) => Ok(Json.toJson(freqs))
      case Left(err) => InternalServerError(s"Error computing the Bonoloto combination frequencies: $err")
    }
  }


  def findMostFrequentMainCombination = Action.async {
    bonolotoStats.computeMostFrequentMainCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the most frequent Bonoloto combination: $err")
    }

  }


  def findLeastFrequentMainCombination = Action.async {
    bonolotoStats.computeLeastFrequentMainCombination() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the least frequent Bonoloto combination: $err")
    }

  }

  def findFrequenciesComplementario() = Action.async {
    bonolotoStats.computeFrequenciesComplementario() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error computing the Bonoloto complementario frequencies: $err")
    }

  }

  def findMostFrequentComplementario = Action.async {
    bonolotoStats.computeMostFrequentComplementario() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the most frequent Bonoloto complementario: $err")
    }

  }

  def findLeastFrequentComplementario = Action.async {
    bonolotoStats.computeLeastFrequentComplementario() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the least frequent Bonoloto complementario: $err")
    }

  }

  def findFrequenciesReintegro() = Action.async {
    bonolotoStats.computeFrequentReintegro() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error computing the Bonoloto reintegro frequencies: $err")
    }

  }

  def findMostFrequentReintegro = Action.async {
    bonolotoStats.computeMostFrequentReintegro() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the most frequent Bonoloto reintegro: $err")
    }

  }

  def findLeastFrequentReintegro = Action.async {
    bonolotoStats.computeLeastFrequentReintegro() map {
      case Right(comb) => Ok(Json.toJson(comb))
      case Left(err) => InternalServerError(s"Error the least frequent Bonoloto reintegro: $err")
    }

  }
  
}

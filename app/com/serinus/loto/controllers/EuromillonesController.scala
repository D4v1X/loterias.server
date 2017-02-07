package com.serinus.loto.controllers

import java.time.LocalDate
import javax.inject.Inject

import com.serinus.loto.RaffleDate
import com.serinus.loto.lotostats.EuromillonesStats
import com.serinus.loto.services.lotteries.EuromillonesLotteryService
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class EuromillonesController @Inject() (euromillonesStats: EuromillonesStats,
                                        euromillonesLotteryService: EuromillonesLotteryService) extends Controller {

  def findLastResult = Action.async {
    euromillonesLotteryService.findLastResult() map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the last Euromillones result: ${err.getMessage}")
    }
  }


  def findResultForDate(date: String) = Action.async {

    //TODO Validation Date or Binding
    val raffleDate: RaffleDate = LocalDate.parse(date)

    euromillonesLotteryService.findResultForDate(raffleDate) map {
      case result => Ok(Json.toJson(result))
    } recover {
      case err => InternalServerError(s"Error retrieving the Euromillones result for date $date: ${err.getMessage}")
    }
  }

}

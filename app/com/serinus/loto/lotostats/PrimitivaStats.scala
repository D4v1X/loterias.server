package com.serinus.loto.lotostats

import javax.inject.Inject

import com.serinus.loto.services.{LotteryService, PrimitivaStatsService}
import com.serinus.loto.utils.Constants
import com.serinus.loto.{Freq, FreqMap, StatsError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class PrimitivaStats @Inject() (primitivaStatsService: PrimitivaStatsService,
                                lotteryService: LotteryService) {

  def findLastResult() = {
    lotteryService.getLotteryLastResultOf(Constants.TM_LOTTERY_PRIMITIVA_NAME)
  }


  def computeFrequencies(): Future[Either[StatsError, FreqMap]] = {
    primitivaStatsService.findAllPrimitivaMainResults() map { resList =>
      Right(doComputeFrequencies(resList))
    } recover {
      case err => Left(s"${err.getMessage}")
    }
  }


  private def doComputeFrequencies(results: Seq[String]): FreqMap = {
    val freqs = results.foldRight(Map.empty: Map[Int, Int])((value, freqs) => {
      val numbers = value.split(Constants.RESULTS_SEPARATOR)
      val numbersFreqs = numbers.map(_.toInt).groupBy(identity).mapValues(_.length)
      numbersFreqs.foldRight(freqs)((elem, map) => map.updated(elem._1, map.getOrElse(elem._1, 0) + elem._2))
    })
    freqs.toList.map(f => Freq(f._1, f._2)).sortWith(_.frequency > _.frequency)
  }

}
package com.serinus.loto.lotostats

import javax.inject.Inject

import com.serinus.loto.services.stats.BonolotoStatsService
import com.serinus.loto.utils.Constants
import com.serinus.loto.{Freq, FreqMap, StatsError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BonolotoStats @Inject() (bonolotoStatsService: BonolotoStatsService) {

  def computeFrequenciesMainCombination(): Future[Either[String, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllMainResults()) map {
      case Right(freqs) => Right(freqs)
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }

  }

  def computeMostFrequentMainCombination(): Future[Either[StatsError, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllMainResults()) map {
      case Right(freqs) => Right(freqs.toList.take(6))
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }
  }


  def computeLeastFrequentMainCombination(): Future[Either[StatsError, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllMainResults()) map {
      case Right(freqs) => Right(freqs.toList.drop(43))
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }
  }

  def computeFrequenciesComplementario(): Future[Either[String, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllComplementerioResults()) map {
      case Right(freqs) => Right(freqs)
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }

  }

  def computeMostFrequentComplementario(): Future[Either[StatsError, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllComplementerioResults()) map {
      case Right(freqs) => Right(freqs.toList.take(1))
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }
  }


  def computeLeastFrequentComplementario(): Future[Either[StatsError, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllComplementerioResults()) map {
      case Right(freqs) => Right(freqs.toList.drop(48))
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }
  }

  def computeFrequentReintegro(): Future[Either[String, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllReintegroResults()) map {
      case Right(freqs) => Right(freqs)
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }

  }

  def computeMostFrequentReintegro(): Future[Either[StatsError, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllReintegroResults()) map {
      case Right(freqs) => Right(freqs.toList.take(1))
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }
  }


  def computeLeastFrequentReintegro(): Future[Either[StatsError, FreqMap]] = {
    computeFrequenciesOf(bonolotoStatsService.findAllReintegroResults()) map {
      case Right(freqs) => Right(freqs.toList.drop(9))
      case err @ Left(_) => err
    } recover {
      case err => Left(s"${err.getMessage}")
    }
  }


  def computeFrequenciesOf(results: Future[Seq[String]]): Future[Either[String, FreqMap]] = {
    results map { resList =>
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

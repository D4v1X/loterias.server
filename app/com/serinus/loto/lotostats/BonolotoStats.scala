package com.serinus.loto.lotostats

import javax.inject.Inject

import com.serinus.loto.services.stats.BonolotoStatsService
import com.serinus.loto.utils.Constants
import com.serinus.loto.{Freq, FreqMap, StatsError}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BonolotoStats @Inject() (bonolotoStatsService: BonolotoStatsService) {

  }

}

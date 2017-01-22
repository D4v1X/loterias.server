package com.serinus.loto.utils

import com.serinus.loto.types.{CombinationPartName, LotteryName}

object Constants {

  // general config
  final val SPANISH_LOCALE_CODE = "es"


  // custom JOOQ generation properties
  final val JOOQ_POJOS_NEW_PACKAGE = "com.serinus.loto.model.pojos"
  final val JOOQ_POJOS_DEFAULT_PACKAGE_SUFFIX = "pojos"


  // Actor names
  final val CUPONAZO_ONCE_SCRAPER_NAME = "CUPONAZO_ONCE_SCRAPER_ACTOR"
  final val EUROMILLONES_SCRAPER_NAME = "EUROMILLONES_SCRAPER_ACTOR"
  final val PRIMITIVA_SCRAPER_NAME = "PRIMITIVA_SCRAPER_ACTOR"
  final val BONOLOTO_SCRAPER_NAME = "BONOLOTO_SCRAPER_ACTOR"
  final val GORDO_SCRAPER_NAME = "GORDO_SCRAPER_ACTOR"
  final val LOTOTURF_SCRAPER_NAME = "LOTOTURF_SCRAPER_ACTOR"


  //
  // CUPONAZO ONCE
  //
  final val TM_LOTTERY_CUPONAZO_ONCE_NAME: LotteryName = "Cuponazo Once"
  final val TM_COMB_PART_CUPONAZO_COMB_GANADORA: CombinationPartName = "Combinación ganadora"
  final val TM_COMB_PART_CUPONAZO_COMB_GANADORA_SERIE: CombinationPartName = "Serie"
  final val TM_COMB_PART_CUPONAZO_COMB_GANADORA_REINT: CombinationPartName = "Reintegro"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_1: CombinationPartName = "Número Adicional 1"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_1_SERIE: CombinationPartName = "Serie Número Adicional 1"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_2: CombinationPartName = "Número Adicional 2"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_2_SERIE: CombinationPartName = "Serie Número Adicional 2"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_3: CombinationPartName = "Número Adicional 3"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_3_SERIE: CombinationPartName = "Serie Número Adicional 3"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_4: CombinationPartName = "Número Adicional 4"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_4_SERIE: CombinationPartName = "Serie Número Adicional 4"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_5: CombinationPartName = "Número Adicional 5"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_5_SERIE: CombinationPartName = "Serie Número Adicional 5"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_6: CombinationPartName = "Número Adicional 6"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_6_SERIE: CombinationPartName = "Serie Número Adicional 6"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_7: CombinationPartName = "Número Adicional 7"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_7_SERIE: CombinationPartName = "Serie Número Adicional 7"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_8: CombinationPartName = "Número Adicional 8"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_8_SERIE: CombinationPartName = "Serie Número Adicional 8"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_9: CombinationPartName = "Número Adicional 9"
  final val TM_COMB_PART_CUPONAZO_ADDITIONAL_NUM_9_SERIE: CombinationPartName = "Serie Número Adicional 9"

  //
  // EUROMILLONES
  //
  final val TM_LOTTERY_EUROMILLONES_NAME: LotteryName = "Euromillones"
  final val TM_COMB_PART_EUROMILLONES_COMB_NAME: CombinationPartName = "Combinación ganadora"
  final val TM_COMB_PART_EUROMILLONES_ESTRE_NAME: CombinationPartName = "Estrellas"
  final val TM_COMB_PART_EUROMILLONES_MILLON_NAME: CombinationPartName = "El Millón"

  //
  // LA PRIMITIVA
  //
  final val TM_LOTTERY_PRIMITIVA_NAME: LotteryName = "La Primitiva"
  final val TM_COMB_PART_PRIMITIVA_COMB_NAME: CombinationPartName = "Combinación ganadora"
  final val TM_COMB_PART_PRIMITIVA_COMPL_NAME: CombinationPartName = "Complementario"
  final val TM_COMB_PART_PRIMITIVA_REINT_NAME: CombinationPartName = "Reintegro"
  final val TM_COMB_PART_PRIMITIVA_JOKER_NAME: CombinationPartName = "Joker"

  //
  // LA BONOLOTO
  //
  final val TM_LOTTERY_BONOLOTO_NAME: LotteryName = "Bonoloto"
  final val TM_COMB_PART_BONOLOTO_COMB_NAME: CombinationPartName = "Combinación ganadora"
  final val TM_COMB_PART_BONOLOTO_COMPL_NAME: CombinationPartName = "Complementario"
  final val TM_COMB_PART_BONOLOTO_REINT_NAME: CombinationPartName = "Reintegro"

  //
  // El GORDO
  //
  val TM_LOTTERY_GORDO_NAME: LotteryName = "El Gordo de La Primitiva"
  val TM_COMB_PART_GORDO_COMB_NAME: CombinationPartName = "Combinación ganadora"
  val TM_COMB_PART_GORDO_REINT_NAME: CombinationPartName = "Reintegro"

  //
  // LOTOTURF
  //
  val TM_LOTTERY_LOTOTURF_NAME: LotteryName = "Lototurf"
  val TM_COMB_PART_LOTOTURF_COMB_NAME: CombinationPartName = "Combinación ganadora"
  val TM_COMB_PART_LOTOTURF_CABALLO_NAME: CombinationPartName = "Caballo"
  val TM_COMB_PART_LOTOTURF_REINT_NAME: CombinationPartName = "Reintegro"

}

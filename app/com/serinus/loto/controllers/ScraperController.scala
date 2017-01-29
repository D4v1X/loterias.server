package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.services.ScraperService
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ScraperController @Inject() (scraperService: ScraperService) extends Controller {


  def scrapCuponazo: Action[AnyContent] = Action.async {

    scraperService.scrapCuponazo
    Future {
      Ok("")
    }

  }

  def scrapHistoricCuponazo: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricCuponazo
    Future {
      Ok("")
    }

  }

  def scrapEuromillones: Action[AnyContent] = Action.async {
    scraperService.scrapEuromillones
    Future {
      Ok("")
    }
  }

  def scrapHistoricEuromillones: Action[AnyContent] = Action.async {
    scraperService.scrapHistoricEuromillones
    Future {
      Ok("")
    }
  }

  def scrapPrimitiva: Action[AnyContent] = Action.async {

    scraperService.scrapPrimitiva
    Future {
      Ok("")
    }

  }

  def scrapHistoricPrimitiva: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricPrimitiva
    Future {
      Ok("")
    }

  }

  def scrapBonoloto: Action[AnyContent] = Action.async {

    scraperService.scrapBonoloto
    Future {
      Ok("")
    }

  }

  def scrapHistoricBonoloto: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricBonoloto
    Future {
      Ok("")
    }

  }

  def scrapGordo: Action[AnyContent] = Action.async {

    scraperService.scrapGordo
    Future {
      Ok("")
    }

  }

  def scrapHistoricGordo: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricGordo
    Future {
      Ok("")
    }

  }

  def scrapLototurf: Action[AnyContent] = Action.async {

    scraperService.scrapLototurf
    Future {
      Ok("")
    }

  }

  def scrapHistoricLototurf: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricLototurf
    Future {
      Ok("")
    }

  }


  def scrapAllHistoricData: Action[AnyContent] = Action.async {

    scraperService.scrapAllHistoricData
    Future {
      Ok("")
    }
  }

}

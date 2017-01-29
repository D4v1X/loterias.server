package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.services.ScraperService
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class ScraperController @Inject() (scraperService: ScraperService) extends Controller {


  def scrapCuponazo: Action[AnyContent] = Action.async {

    scraperService.scrapCuponazoParser
    Future {
      Ok("")
    }

  }

  def scrapHistoricCuponazo: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricCuponazoParser
    Future {
      Ok("")
    }

  }

  def scrapEuromillones: Action[AnyContent] = Action.async {
    scraperService.scrapEuromillonesParser
    Future {
      Ok("")
    }
  }

  def scrapHistoricEuromillones: Action[AnyContent] = Action.async {
    scraperService.scrapHistoricEuromillonesParser
    Future {
      Ok("")
    }
  }

  def scrapPrimitiva: Action[AnyContent] = Action.async {

    scraperService.scrapPrimitivaParser
    Future {
      Ok("")
    }

  }

  def scrapHistoricPrimitiva: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricPrimitivaParser
    Future {
      Ok("")
    }

  }

  def scrapBonoloto: Action[AnyContent] = Action.async {

    scraperService.scrapBonolotoParser
    Future {
      Ok("")
    }

  }

  def scrapHistoricBonoloto: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricBonolotoParser
    Future {
      Ok("")
    }

  }

  def scrapGordo: Action[AnyContent] = Action.async {

    scraperService.scrapGordoParser
    Future {
      Ok("")
    }

  }

  def scrapHistoricGordo: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricGordoParser
    Future {
      Ok("")
    }

  }

  def scrapLototurf: Action[AnyContent] = Action.async {

    scraperService.scrapLototurfParser
    Future {
      Ok("")
    }

  }

  def scrapHistoricLototurf: Action[AnyContent] = Action.async {

    scraperService.scrapHistoricLototurfParser
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

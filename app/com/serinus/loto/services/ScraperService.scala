package com.serinus.loto.services

import java.time.LocalDate
import javax.inject.{Inject, Named}

import akka.actor.ActorRef
import com.serinus.loto.scrapers.ScraperMessages
import com.serinus.loto.utils.{Constants, DB}

class ScraperService @Inject()(db: DB,
                               @Named(Constants.CUPONAZO_ONCE_SCRAPER_NAME) cuponazoOnceScaper: ActorRef,
                               @Named(Constants.EUROMILLONES_SCRAPER_NAME) euromillonesScraper: ActorRef,
                               @Named(Constants.PRIMITIVA_SCRAPER_NAME) primitivaScraper: ActorRef,
                               @Named(Constants.BONOLOTO_SCRAPER_NAME) bonolotoScraper: ActorRef,
                               @Named(Constants.GORDO_SCRAPER_NAME) gordoScraper: ActorRef,
                               @Named(Constants.LOTOTURF_SCRAPER_NAME) lototurfScraper: ActorRef) {



  def scrapCuponazo = {
    cuponazoOnceScaper ! ScraperMessages.ScrapCuponazo
  }


  def scrapHistoricCuponazo = {
    cuponazoOnceScaper ! ScraperMessages.ScrapHistoricCuponazo(Some(LocalDate.of(2012, 6, 8)), None)
  }


  def scrapEuromillones = {
    euromillonesScraper ! ScraperMessages.ScrapEuromillones
  }

  def scrapHistoricEuromillones = {
    euromillonesScraper ! ScraperMessages.ScrapHistoricEuromillones(Some(LocalDate.of(2004, 5, 14)), None)
  }


  def scrapPrimitiva = {
    primitivaScraper ! ScraperMessages.ScrapPrimitiva
  }

  def scrapHistoricPrimitiva = {
    primitivaScraper ! ScraperMessages.ScrapHistoricPrimitiva(Some(LocalDate.of(2004, 5, 13)), None)
  }


  def scrapBonoloto = {
    bonolotoScraper ! ScraperMessages.ScrapBonoloto
  }

  def scrapHistoricBonoloto = {
    bonolotoScraper ! ScraperMessages.ScrapHistoricBonoloto(Some(LocalDate.of(2004, 5, 18)), None)
  }


  def scrapGordo = {
    gordoScraper ! ScraperMessages.ScrapGordo
  }

  def scrapHistoricGordo = {
    gordoScraper ! ScraperMessages.ScrapHistoricGordo(Some(LocalDate.of(2004, 5, 16)), None)
  }


  def scrapLototurf = {
    lototurfScraper ! ScraperMessages.ScrapLototurf
  }

  def scrapHistoricLototurf = {
    lototurfScraper ! ScraperMessages.ScrapHistoricLototurf(Some(LocalDate.of(2006, 4, 16)), None)
  }


  def scrapAllHistoricData = {
    cuponazoOnceScaper ! ScraperMessages.ScrapHistoricCuponazo(Some(LocalDate.of(2012, 6, 8)), None)
    euromillonesScraper ! ScraperMessages.ScrapHistoricEuromillones(Some(LocalDate.of(2004, 5, 14)), None)
    primitivaScraper ! ScraperMessages.ScrapHistoricPrimitiva(Some(LocalDate.of(2004, 5, 13)), None)
    bonolotoScraper ! ScraperMessages.ScrapHistoricBonoloto(Some(LocalDate.of(2004, 5, 18)), None)
    gordoScraper ! ScraperMessages.ScrapHistoricGordo(Some(LocalDate.of(2004, 5, 16)), None)
    lototurfScraper ! ScraperMessages.ScrapHistoricLototurf(Some(LocalDate.of(2006, 4, 16)), None)
  }

}

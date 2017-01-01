package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.model.caseclasses._
import com.serinus.loto.services.TestService
import play.api.libs.json.Json
import play.api.mvc._

import scala.collection.JavaConversions._
import scala.concurrent.{ExecutionContext, Future}

class TestController @Inject()
  (testService: TestService)
  (implicit ec: ExecutionContext) extends Controller {


  implicit val tmLotteryWrites = Json.writes[TmLotteryCC]
  implicit val tmCombinationWrites = Json.writes[TmCombinationPartCC]
  implicit val twResultWrites = Json.writes[TwResultCC]


  def test = Action.async {
    testService.hello.map { tmLottery =>

      Ok(Json.toJson(TmLotteryCC.fromPojo(tmLottery)))

    }
  }

  def testLottery = Action.async {
    testService.testLottery.map { tmLotteryList =>

      Ok(Json.toJson(TmLotteryCC.fromPojoList(tmLotteryList)))

    }
  }

  def testCombination = Action.async {
    testService.testCombination.map { tmCombinationList =>

      Ok(Json.toJson(TmCombinationPartCC.fromPojoList(tmCombinationList)))

    }
  }

  def testResult = Action.async {
    testService.testResult.map { twResultList =>

      Ok(Json.toJson(TwResultCC.fromPojoList(twResultList)))

    }
  }

  def testCuponazo = Action.async {

    testService.testCuponazoParser
    Future {
      Ok("")
    }

  }

  def testHistoricCuponazo = Action.async {

    testService.testHistoricCuponazoParser
    Future {
      Ok("")
    }

  }

  def testPrimitiva = Action.async {

    testService.testPrimitivaParser
    Future {
      Ok("")
    }

  }

  def testBonoloto = Action.async {

    testService.testBonolotoParser
    Future {
      Ok("")
    }

  }

  def testGordo = Action.async {

    testService.testGordoParser
    Future {
      Ok("")
    }

  }

  def testLototurf = Action.async {

    testService.testLototurfParser
    Future {
      Ok("")
    }

  }

}

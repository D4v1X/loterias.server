package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.model.caseclasses.pojos.{TmCombinationPartCC, TmLotteryCC, TwResultCC}
import com.serinus.loto.services.TestService
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Controller}

import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext


class TestController @Inject()(testService: TestService)
                              (implicit ec: ExecutionContext)
  extends Controller {


  def testLottery: Action[AnyContent] = Action.async {
    testService.testLottery.map { tmLotteryList =>

      Ok(Json.toJson(TmLotteryCC.fromPojoList(tmLotteryList)))

    }
  }

  def testCombination: Action[AnyContent] = Action.async {
    testService.testCombination.map { tmCombinationList =>

      Ok(Json.toJson(TmCombinationPartCC.fromPojoList(tmCombinationList)))

    }
  }

  def testResult: Action[AnyContent] = Action.async {
    testService.testResult.map { twResultList =>

      Ok(Json.toJson(TwResultCC.fromPojoList(twResultList)))

    }
  }

}

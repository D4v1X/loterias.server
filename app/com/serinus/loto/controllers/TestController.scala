package com.serinus.loto.controllers

import javax.inject.Inject

import com.serinus.loto.model.caseclasses._
import com.serinus.loto.services.TestService
import play.api.libs.json.Json
import play.api.mvc._
import scala.concurrent.ExecutionContext

class TestController @Inject()
  (testService: TestService)
  (implicit ec: ExecutionContext) extends Controller {


  implicit val tmLotteryWrites = Json.writes[TmLotteryCC]


  def test = Action.async {
    testService.hello.map { tmLottery =>

      Ok(Json.toJson(TmLotteryCC.fromPojo(tmLottery)))

    }
  }

}

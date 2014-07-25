package controllers

import play.api._
import play.api.mvc._
import models.YandexRequest
import play.api.libs.ws._
import play.api.libs.ws.WS.WSRequestHolder
import scala.concurrent.{Await, Future}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.duration.Duration

/**
 * Created by Dmitry Meshkov on 24.07.2014.
 */
object Search extends Controller {
  def search(query: List[String]) = Action {
    val futureRequests = query.map(x => Future {
      new YandexRequest(x).links
    })

    val d2 = Duration(10, "s")
    val res = futureRequests map { x => Await.result(x, d2)}
    val domains = res.flatten.toSet
    Ok(domains.toString())
  }

}

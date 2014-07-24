package controllers

import play.api._
import play.api.mvc._
import models.YandexRequest
import play.api.libs.ws._
import play.api.libs.ws.WS.WSRequestHolder


/**
 * Created by Dmitry Meshkov on 24.07.2014.
 */
object Search extends Controller {
  def search(query: List[String]) = Action {
    val requests = query.map(new YandexRequest(_))
    // TODO make async calls dor all queries
    Ok(requests.head.domains.toString())
  }
}

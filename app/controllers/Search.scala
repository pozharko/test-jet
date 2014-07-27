package controllers

import play.api.http.ContentTypes
import play.api.libs.json.{Json, JsObject, JsNumber}
import play.api.mvc._
import models.YandexRequest

/**
 * @author Dmitry Meshkov
 * @since 24.07.2014
 */
object Search extends Controller {
  /**
   * Request Yandex Blogs search for keywords in the query list
   * and show second level domain frequency
   */
  def search(query: List[String]) = Action {
    val stats = new YandexRequest(query).stats
    val statsJs = stats map (x => x._1 -> JsNumber(x._2))
    val json = JsObject(statsJs.toSeq.sortWith(_._1 < _._1))

    Ok(Json.prettyPrint(json)).as(ContentTypes.JSON)
  }

}

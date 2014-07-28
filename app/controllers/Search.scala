/**
 * @author Dmitry Meshkov
 * @since 24.07.2014
 */
package controllers

import play.api.http.ContentTypes
import play.api.libs.json.Json
import play.api.mvc._
import models.BlogSearch._

object Search extends Controller {
  /**
   * Request Yandex Blogs search for keywords in the query list
   * and show second level domain frequency
   */
  def search(query: List[String]) = Action {
    val json = new YandexBlogSearch(query).stats.asJson

    Ok(Json.prettyPrint(json)).as(ContentTypes.JSON)
  }

}

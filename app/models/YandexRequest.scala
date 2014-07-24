package models

import play.api.libs.ws._
import play.api.libs.ws.WS.WSRequestHolder
import scala.concurrent.Future
import scala.xml._
import java.text.{SimpleDateFormat, ParseException}
import java.net.URL
import scala.util.matching.Regex

/**
 * @author Dmitry Meshkov
 * @since 24.07.2014
 */
class YandexRequest(query: String) {
//    val holder : WSRequestHolder = WS.url("http://blogs.yandex.ru/search.rss").withQueryString("text" -> query)
//    val rss : Future[Response] = holder.get()

  private val url = "http://blogs.yandex.ru/search.rss?text=" + query
  private val links = (XML.load(url) \\ "item").map(x => (x \\ "link").text).toList
  val domains = links.map(getSecondLevelDomain(_))

  // TODO parse only second level domain
  private def getSecondLevelDomain(str: String) = {
    new URL(str).getHost()
//    val domain =  new URL(str).getHost()
//    val pattern = "\.\\w\.\\w$".r
//    pattern findFirstIn domain
  }

}
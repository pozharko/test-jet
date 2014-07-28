/**
 * @author Dmitry Meshkov
 * @since 24.07.2014
 */
package models

import com.google.common.net.InternetDomainName
import scala.xml._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import java.net.URL
import play.api.libs.json.{JsObject, JsNumber}

object BlogSearch {

  /**
   * Search for keywords in yandex blogs
   */
  class YandexBlogSearch(keywords: List[String]) {
    /**
     * Wait response for 10 seconds
     */
    private val awaitDuration = Duration(10, "s")
    /**
     * Request Yandex in 10 threads
     */
    private implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

    /**
     * Sequence of requests to Yandex
     */
    private val requests = {
      /**
       * Urls to request
       */
      val urls = keywords.map("http://blogs.yandex.ru/search.rss?text=" + _)
      /**
       * Request pool.
       * Takes first 10 links from each request
       */
      val futureRequests = urls.map(url => Future {
        (XML.load(url) \\ "item").map(x => (x \\ "link").text).toSet.take(10)
      })
      /**
       * Aggregated request
       */
      Future.sequence(futureRequests)
    }

    /**
     * Links from all requests
     */
    lazy val stats = new SearchStats(Await.result(requests, awaitDuration).flatten.distinct)

  }

  /**
   * Statistics of search results
   */
  class SearchStats(data: List[String]) {
    /**
     * Second level domains from links
     */
    private val domains: List[String] = data.map({ x =>
      val host = new URL(x).getHost
      val domainName = InternetDomainName.from(host)
      domainName.topPrivateDomain().toString
    })
    /**
     * Domains frequency statistics
     */
    private val stats = domains.map((x: String) => (x, domains.count(_ == x))).toSet

    def asJson = {
      val statsJs = stats.map(x => x._1 -> JsNumber(x._2))
      JsObject(statsJs.toSeq.sortWith(_._1 < _._1))
    }
  }

}

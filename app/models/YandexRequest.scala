package models

import com.google.common.net.InternetDomainName
import scala.xml._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import java.net.URL

/**
 * @author Dmitry Meshkov
 * @since 24.07.2014
 */
class YandexRequest(query: List[String]) {
  /**
   * Wait response for 10 seconds
   */
  private val awaitDuration = Duration(10, "s")
  /**
   * Request Yandex in 10 threads
   */
  implicit val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

  /**
   * Urls to request
   */
  private val urls = query map ("http://blogs.yandex.ru/search.rss?text=" + _)
  /**
   * Request pool.
   * Takes first 10 links from each request
   */
  private val futureRequests = urls map (url => Future {
    (XML.load(url) \\ "item").map(x => (x \\ "link").text).toSet.take(10)
  })
  /**
   * Aggregated request
   */
  private val aggregated = Future.sequence(futureRequests)
  /**
   * Links from all requests
   */
  private lazy val links = Await.result(aggregated, awaitDuration).flatten.distinct
  /**
   * Second level domains from links
   */
  private lazy val domains: List[String] = links map ({ x =>
    val host = new URL(x).getHost
    val domainName = InternetDomainName.from(host)
    domainName.topPrivateDomain().toString
  })
  /**
   * Domains frequency statistics
   */
  lazy val stats = domains map ((x: String) => (x, domains.count(_ == x))) toSet

}

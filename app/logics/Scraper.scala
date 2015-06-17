package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import collection.JavaConverters._
import scala.util.control.Exception.allCatch

// Scraper for livedoor news
object Scraper {

  /** Return article text and html */
  def article(guid: Long): Option[(String, String)] = {
    val url = s"http://news.livedoor.com/article/detail/$guid/"
    allCatch opt { Jsoup.connect(url).get } map { doc =>
      val content = doc.select(".articleBody").asScala.head
      (content.text, content.html)
    }
  }

}

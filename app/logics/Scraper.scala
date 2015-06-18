package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import collection.JavaConverters._
import scala.util.control.Exception.allCatch

// Scraper for livedoor news
object Scraper {

  /** Return article text and html */
  def article(guid: Long): Option[(String, String, Option[java.net.URL])] = {
    val url = s"http://news.livedoor.com/article/detail/$guid/"
    allCatch opt { Jsoup.connect(url).get } map { doc =>
      val content = doc.select(".articleBody").asScala.head
      val image =
        doc.select(".articleImage img").asScala.headOption.map { elm =>
          new java.net.URL(elm.attr("src"))
        }
      (content.text, content.html, image)
    }
  }

}

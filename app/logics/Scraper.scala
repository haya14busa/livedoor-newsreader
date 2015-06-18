package logics

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import collection.JavaConverters._
import scala.util.control.Exception.allCatch

// Scraper for livedoor news
object Scraper {

  /** Return article text and html */
  def article(guid: Long): Option[(String, String, Option[java.net.URL])] = {
    Thread.sleep(5000)
    play.Logger.info(s"[Scraper] start scraping: $guid")
    val url = s"http://news.livedoor.com/article/detail/$guid/"
    val r = allCatch opt { Jsoup.connect(url).get } flatMap { doc =>
      doc.select("[itemprop=articleBody]").asScala.headOption map { content =>
        val image =
          doc.select(".articleImage img").asScala.headOption.map { elm =>
            new java.net.URL(elm.attr("src"))
          }
        (content.text, content.html, image)
      }
    }
    if (r.isEmpty) {
      play.Logger.debug(s"[Scraper] error occurs with $guid")
    }
    r
  }

}

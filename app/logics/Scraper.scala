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

  def parseFeed(category: models.Category): Option[models.Feed] = allCatch opt {
    val root = scala.xml.XML.load(category.rss)
    val channel = root \ "channel"
    models.Feed(
      title = (channel \ "title").text,
      description = (channel \ "description").text,
      lastBuildDate = allCatch opt { utils.Date.parseRFC2822((channel \ "lastBuildDate").text) },
      articles = (channel \ "item").flatMap(item => this.parseArticle(item, category))
    )
  }

  def parseArticle(item: scala.xml.Node, category: models.Category): Option[models.RssArticle] = allCatch opt {
    val link = new java.net.URL((item \ "link").text)
    val guid = link.getPath.split("/").last.toLong
    models.RssArticle(
      guid = guid,
      cgid = category.cgid,
      title = (item \ "title").text,
      description = (item \ "description").text,
      pubdate = (utils.Date.parseRFC2822((item \ "pubDate").text)),
      link = link
    )
  }

}

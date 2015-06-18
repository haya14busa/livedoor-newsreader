package models
import play.api.libs.json.Json
import scala.util.control.Exception.allCatch

import models.Tables.ArticlesRow

case class Article(
  guid:        Long,
  cgid:        String,
  title:       String,
  description: String,
  pubdate:     java.util.Date,
  link:        java.net.URL,
  content:     String,
  html:        String,
  image:       Option[java.net.URL]
)

case class RssArticle(
  guid:        Long,
  cgid:        String,
  title:       String,
  description: String,
  pubdate:     java.util.Date,
  link:        java.net.URL
)

object RssArticle {
  import utils.json.URL.dateJsonWrites
  implicit val rssarticleWrites = Json.writes[RssArticle]

  /** Scraping occurs! */
  def toArticle(r: RssArticle): Option[Article] = {
    logics.Scraper.article(r.guid) map {
      case (content, html, image) =>
        Article(
          guid = r.guid,
          cgid = r.cgid,
          title = r.title,
          description = r.description,
          pubdate = r.pubdate,
          link = r.link,
          content = content,
          html = html,
          image = image
        )
    }
  }
}

case class Feed(
  title:         String,
  description:   String,
  lastBuildDate: Option[java.util.Date],
  articles:      Seq[RssArticle]
)

object Article {
  import utils.json.URL.dateJsonWrites
  implicit val articleWrites = Json.writes[Article]
}

object Feed {
  import utils.json.URL.dateJsonWrites
  implicit val articleWrites = Json.writes[Article]
  implicit val feedWrites = Json.writes[Feed]
}

case class Category(
  cgid:  String, // CateGory ID
  name:  String,
  rss:   java.net.URL,
  order: Int
)

object Category {
  import utils.json.URL.dateJsonWrites
  implicit val categoryWrites = Json.writes[Category]
}

object Categories {
  // 主要     : http://news.livedoor.com/topics/rss/top.xml
  // 国内     : http://news.livedoor.com/topics/rss/dom.xml
  // 海外     : http://news.livedoor.com/topics/rss/int.xml
  // IT 経済  : http://news.livedoor.com/topics/rss/eco.xml
  // 芸能     : http://news.livedoor.com/topics/rss/ent.xml
  // スポーツ : http://news.livedoor.com/topics/rss/spo.xml
  // 映画     : http://news.livedoor.com/rss/summary/52.xml
  // グルメ   : http://news.livedoor.com/topics/rss/gourmet.xml
  // 女子     : http://news.livedoor.com/topics/rss/love.xml
  // トレンド : http://news.livedoor.com/topics/rss/trend.xml
  val categories: List[Category] =
    for {
      (t, order) <- List(
        ("top", "主要", "http://news.livedoor.com/topics/rss/top.xml"),
        ("dom", "国内", "http://news.livedoor.com/topics/rss/dom.xml"),
        ("int", "海外", "http://news.livedoor.com/topics/rss/int.xml"),
        ("eco", "IT 経済", "http://news.livedoor.com/topics/rss/eco.xml"),
        ("ent", "芸能", "http://news.livedoor.com/topics/rss/ent.xml"),
        ("spo", "スポーツ", "http://news.livedoor.com/topics/rss/spo.xml"),
        ("movie", "映画", "http://news.livedoor.com/rss/summary/52.xml"),
        ("gourmet", "グルメ", "http://news.livedoor.com/topics/rss/gourmet.xml"),
        ("love", "女子", "http://news.livedoor.com/topics/rss/love.xml"),
        ("trend", "トレンド", "http://news.livedoor.com/topics/rss/trend.xml")
      ).zipWithIndex
    } yield {
      val (cgid, name, rss) = t
      Category(cgid, name, new java.net.URL(rss), order)
    }
}


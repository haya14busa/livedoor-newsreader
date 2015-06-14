package models
import play.api.libs.json.Json
import scala.util.control.Exception.allCatch

case class Article(
  guid:        String,
  title:       String,
  description: String,
  pubDate:     java.util.Date,
  link:        java.net.URL
)

case class Feed(
  title:         String,
  description:   String,
  lastBuildDate: Option[java.util.Date],
  articles:      Seq[Article]
)

object Article {
  import utils.json.URL.dateJsonWrites
  implicit val articleWrites = Json.writes[Article]

  // TODO?: return as Either[Throwable, T]
  def fromXml(item: scala.xml.Node): Option[Article] = allCatch opt {
    Article(
      guid = (item \ "guid").text,
      title = (item \ "title").text,
      description = (item \ "description").text,
      pubDate = utils.Date.parseRFC2822((item \ "pubDate").text),
      link = new java.net.URL((item \ "link").text)
    )
  }
}

object Feed {
  import utils.json.URL.dateJsonWrites
  implicit val feedWrites = Json.writes[Feed]

  // TODO?: return as Either[Throwable, T]
  def fromXml(root: scala.xml.Node): Option[Feed] = allCatch opt {
    val channel = root \ "channel"
    Feed(
      title = (channel \ "title").text,
      description = (channel \ "description").text,
      lastBuildDate = allCatch opt { utils.Date.parseRFC2822((channel \ "lastBuildDate").text) },
      articles = (channel \ "item") flatMap Article.fromXml
    )
  }
}

case class Category(
  cgid:  String, // CateGory ID
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
        ("top", "http://news.livedoor.com/topics/rss/top.xml"),
        ("dom", "http://news.livedoor.com/topics/rss/dom.xml"),
        ("int", "http://news.livedoor.com/topics/rss/int.xml"),
        ("eco", "http://news.livedoor.com/topics/rss/eco.xml"),
        ("ent", "http://news.livedoor.com/topics/rss/ent.xml"),
        ("spo", "http://news.livedoor.com/topics/rss/spo.xml"),
        ("movie", "http://news.livedoor.com/rss/summary/52.xml"),
        ("gourmet", "http://news.livedoor.com/topics/rss/gourmet.xml"),
        ("love", "http://news.livedoor.com/topics/rss/love.xml"),
        ("trend", "http://news.livedoor.com/topics/rss/trend.xml")
      ).zipWithIndex
    } yield {
      val (cgid, rss) = t
      Category(cgid, new java.net.URL(rss), order)
    }
}


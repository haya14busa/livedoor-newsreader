package models
import play.api.libs.json.Json
import scala.util.control.Exception.allCatch

case class Article(
  guid:        Long,
  title:       String,
  description: String,
  pubDate:     java.util.Date,
  link:        java.net.URL,
  content:     String,
  html:        String,
  image:       Option[java.net.URL]
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
    val link = new java.net.URL((item \ "link").text)
    val guid = link.getPath.split("/").last.toLong
    // val (content, html, image) = logics.Scraper.article(guid).get
    val (content, html, image) = ("content", "html", None) // TODO: do not scrape in dev
    Article(
      guid = guid,
      title = (item \ "title").text,
      description = (item \ "description").text,
      pubDate = utils.Date.parseRFC2822((item \ "pubDate").text),
      link = link,
      content = content,
      html = html,
      image = image
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
      articles = (channel \ "item").par.map(Article.fromXml).flatten.seq
    )
  }
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


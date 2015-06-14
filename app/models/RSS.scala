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
  lastBuildDate: java.util.Date,
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
      lastBuildDate = utils.Date.parseRFC2822((channel \ "lastBuildDate").text),
      articles = (channel \ "item") flatMap Article.fromXml
    )
  }
}


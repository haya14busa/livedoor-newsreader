// scalastyle:off public.methods.have.type for Action in controllers
package controllers

import play.api._
import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index(feed().toString))
  }

  def feed() = {
    val url = "http://news.livedoor.com/topics/rss/top.xml"
    val feed = models.Feed.fromXml(scala.xml.XML.load(url))
    feed
  }

}

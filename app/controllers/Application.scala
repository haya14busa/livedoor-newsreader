// scalastyle:off public.methods.have.type for Action in controllers
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

class Application extends Controller {

  def index = Action {
    Ok(views.html.index(Json.toJson(feed()).toString))
  }

  def feed() = {
    val url = "http://news.livedoor.com/topics/rss/top.xml"
    val feed = models.Feed.fromXml(scala.xml.XML.load(url))
    feed
  }

}

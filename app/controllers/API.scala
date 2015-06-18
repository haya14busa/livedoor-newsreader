// scalastyle:off public.methods.have.type for Action in controllers
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

class API extends Controller {

  def categories = Action {
    Ok(Json.toJson(models.Categories.categories))
  }

  def feed(cgid: String) = Action {
    // TODO: better error handling
    (for {
      category <- models.Categories.categories.find(_.cgid == cgid)
      feed <- models.Feed.fromXml(scala.xml.XML.load(category.rss))
    } yield feed).fold[Result](NotFound) { f => Ok(Json.toJson(f)) }
  }

  def article(guid: String) = TODO
  def relatedArticles(guid: String) = TODO

  def crawl = Action {
    logics.Crawl.go()
    Ok("Crawling start")
  }

}

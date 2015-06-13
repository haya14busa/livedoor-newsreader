// scalastyle:off public.methods.have.type for Action in controllers
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

class API extends Controller {

  def categories = TODO
  def feeds(category: String) = TODO
  def article(guid: String) = TODO
  def relatedArticles(guid: String) = TODO

}

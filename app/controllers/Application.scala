// scalastyle:off public.methods.have.type for Action in controllers
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

class Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

}

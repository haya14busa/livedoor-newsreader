// _root_ package

import javax.inject.Inject
import play.api.http._
import play.api.mvc._
import play.api.routing.Router

class RequestHandler @Inject() (router: Router) extends HttpRequestHandler {
  def handlerForRequest(request: RequestHeader) = {
    router.routes.lift(request) match {
      case Some(handler) => {
        play.Logger.info(request.toString)
        (request, handler)
      }
      case None => (request, Action(Results.NotFound))
    }
  }
}

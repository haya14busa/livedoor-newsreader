// scalastyle:off public.methods.have.type for Action in controllers
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.{ Await, Future }

class API extends Controller {

  def categories = Action {
    Ok(Json.toJson(models.Categories.categories))
  }

  def feed(cgid: String) = Action.async {
    dao.ArticleDAO.list(cgid) map { feed =>
      models.Categories.categories.find(_.cgid == cgid)
        .fold[Result](NotFound) { category =>
          Ok(Json.toJson(models.FullRSSFeed(category, feed)))
        }
    }
  }

  def article(guid: Long) = Action.async {
    dao.ArticleDAO.find(guid) map { maybeArticle =>
      maybeArticle.fold[Result](NotFound) { article =>
        Ok(Json.toJson(dao.ArticleDAOConvertion.convertRowToArticle(article)))
      }
    }
  }

  def relatedArticles(guid: Long) = TODO

  def crawl = Action {
    logics.Crawl.go()
    Ok("Crawling start")
  }

}

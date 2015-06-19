package dao

import scala.concurrent.Future
import slick.driver.PostgresDriver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import globals.db
import models.Article
import models.Tables.ArticlesRow

object ArticleDAO {
  val Articles = TableQuery[models.Tables.Articles]
  import ArticleDAOConvertion._

  def insert(article: Article): Future[Unit] = insert(convertArticleToRow(article))
  def insert(article: ArticlesRow): Future[Unit] = {
    this.find(article.guid) withFilter (_.isEmpty) map { _ =>
      db.run(Articles += article).map(_ => ())
    }
  }

  def find(guid: Long): Future[Option[ArticlesRow]] = {
    val q = Articles.filter(a => a.guid === guid)
    db.run(q.result).map(_.headOption)
  }

  def list(cgid: String, limit: Int = 50): Future[List[Article]] = {
    val q = Articles.filter(a => a.cgid === cgid).sortBy(_.pubdate.desc).take(limit)
    db.run(q.result).map(_.toList.map(convertRowToArticle))
  }

  def allDocs(): Future[Set[(Long, String)]] =
    db.run(Articles.map(r => (r.guid, r.content)).result).map(_.toSet)

}

object ArticleDAOConvertion {
  import scala.language.implicitConversions

  implicit def convertRowToArticle(r: ArticlesRow): Article =
    Article(
      guid = r.guid,
      cgid = r.cgid,
      title = r.title,
      description = r.description,
      pubdate = r.pubdate,
      link = new java.net.URL(r.link),
      content = r.content,
      html = r.html,
      image = r.image map (i => new java.net.URL(i))
    )

  implicit def convertArticleToRow(a: Article): ArticlesRow =
    ArticlesRow(
      guid = a.guid,
      cgid = a.cgid,
      title = a.title,
      description = a.description,
      pubdate = new java.sql.Timestamp(a.pubdate.getTime()),
      link = a.link.toString,
      content = a.content,
      html = a.html,
      image = a.image map (_.toString)
    )

}

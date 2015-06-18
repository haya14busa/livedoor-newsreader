package dao

import scala.concurrent.Future
import slick.driver.PostgresDriver.api._
import slick.lifted.Query
import scala.concurrent.ExecutionContext.Implicits.global
import globals.db
import models.Tables.{ Relatedocs, RelatedocsRow }
import models.Article
import dao.ArticleDAO.Articles

object RelatedocsDAO {
  val Relatedocs = TableQuery[models.Tables.Relatedocs]

  def upsert(relatedoc: RelatedocsRow): Future[Unit] =
    this.find(relatedoc.guid) map { existing =>
      existing.fold[Unit] {
        db.run(Relatedocs += relatedoc).map(_ => ())
      } { _ =>
        db.run(qfind(relatedoc.guid).update(relatedoc))
      }
    }

  def find(guid: Long): Future[Option[RelatedocsRow]] =
    db.run(qfind(guid).result).map(_.headOption)

  def qfind(guid: Long): Query[Relatedocs, RelatedocsRow, Seq] =
    Relatedocs.filter(a => a.guid === guid)

  def getRelatedArticles(guid: Long): Future[List[Article]] = {
    val q = (for {
      relatedocs <- Relatedocs if relatedocs.guid === guid
      article <- Articles
      if (article.guid === relatedocs.rank1) ||
        (article.guid === relatedocs.rank2) ||
        (article.guid === relatedocs.rank3) ||
        (article.guid === relatedocs.rank4) ||
        (article.guid === relatedocs.rank5)
    } yield article)
    db.run(q.result).map(_.toList.map(ArticleDAOConvertion.convertRowToArticle))
  }

}

package logics

import models.Feed
import models.{ Article, RssArticle }
import models.{ Category, Categories }
import models.RssArticle.toArticle
import models.Tables.RelatedocsRow

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.collection.parallel.immutable.ParSeq

import logics.DocVector.{ RawDocument, DocumentVector }

object Crawl {
  def go() = {
    dao.ArticleDAO.allDocs() map { existingArticles: Set[(Long, String)] =>
      play.Logger.info(s"[Crawler] start scraping articles ${getRssArticles.length}")
      val ids = existingArticles map (_._1)
      val articles: ParSeq[Article] =
        getRssArticles
          .withFilter(a => !(ids contains a.guid)) // filter existing articles
          .par.flatMap(toArticle)
      play.Logger.info(s"[Crawler] end scraping articles ${articles.length}")
      if (articles.isEmpty)
        play.Logger.info(s"[Crawler] Do not re-calculate related doc because there are no new documents")
      else {
        calcRelatedArticles(articles, existingArticles)
        play.Logger.info("[Crawler] start to insert articles")
        articles foreach dao.ArticleDAO.insert
        play.Logger.info("[Crawler] end inserting articles")
      }
    }
  }

  private def calcRelatedArticles(articles: ParSeq[Article], existingArticles: Set[(Long, String)]) = {
    Future {
      play.Logger.info("[Crawler] start tfidf calculation")
      val dvs: List[DocumentVector] = DocVector.tfidf(
        articles.map(a => RawDocument(guid = a.guid, body = a.content)).toList
          :::
          existingArticles.map(ea => RawDocument(guid = ea._1, body = ea._2)).toList
      )
      dvs.par.foreach { dv =>
        val relatedArticles = DocVector.findSimilarDocs(dv, dvs, n = 5)
        play.Logger.info(s"[Similer #${dv.guid}] ${relatedArticles map (_.guid)}")
        val as = ((relatedArticles.map(r => Some(r.guid)) ::: List.fill[Option[Long]](5)(None)) take 5)
        dao.RelatedocsDAO.upsert(
          RelatedocsRow(
            guid = dv.guid,
            rank1 = as(0),
            rank2 = as(1),
            rank3 = as(2),
            rank4 = as(3),
            rank5 = as(4)
          )
        )
      }
      play.Logger.info("[Crawler] end tfidf calculation")
    }
  }

  private def getRssArticles: ParSeq[RssArticle] =
    getAllFeeds.par.flatMap(_.articles)

  private def getAllFeeds: ParSeq[Feed] =
    Categories.categories.par.flatMap(this.getRssFeed)

  private def getRssFeed(category: Category): Option[Feed] =
    logics.Scraper.parseFeed(category)

}

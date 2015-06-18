package logics

import models.Feed
import models.{ Article, RssArticle }
import models.{ Category, Categories }
import models.RssArticle.toArticle

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

      Future {
        play.Logger.info("[Crawler] start tfidf calculation")
        val dvs: List[DocumentVector] = DocVector.tfidf(
          articles.map(a => RawDocument(guid = a.guid, body = a.content)).toList
            :::
            existingArticles.map(ea => RawDocument(guid = ea._1, body = ea._2)).toList
        )
        dvs.par.foreach { dv =>
          val relatedArticles = DocVector.findSimilarDocs(dv, dvs, n = 3)
          play.Logger.info(s"[Similer #${dv.guid}] ${relatedArticles map (_.guid)}")
          // TODO: insert related articles
        }
        play.Logger.info("[Crawler] end tfidf calculation")
      }

      Future {
        play.Logger.info("[Crawler] start to insert articles")
        articles foreach dao.ArticleDAO.insert
        play.Logger.info("[Crawler] end inserting articles")
      }
    }
  }

  private def getRssArticles: ParSeq[RssArticle] =
    getAllFeeds.par.flatMap(_.articles)

  private def getAllFeeds: ParSeq[Feed] =
    Categories.categories.par.flatMap(this.getRssFeed)

  private def getRssFeed(category: Category): Option[Feed] =
    Feed.fromXml(scala.xml.XML.load(category.rss))

}

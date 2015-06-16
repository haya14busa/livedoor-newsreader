package logics

import java.text.Normalizer
import org.atilika.kuromoji.Tokenizer
import org.atilika.kuromoji.Token
import scala.collection.immutable.Set

object DocVector {
  import scala.collection.JavaConversions._

  sealed abstract class Document
  case class RawDocument(id: Int, body: String) extends Document
  case class DocumentVector(id: Int, body: String, tfidf: TFIDF) extends Document

  type TF = Map[String, Int] // term -> term frequency
  type DF = Map[String, Int] // term -> document frequency
  type TFIDF = Map[String, Double] // term -> tfidf

  private val tokenizer = Tokenizer.builder.mode(Tokenizer.Mode.NORMAL).build

  private def tokenize(text: String): List[Token] = tokenizer.tokenize(text).toList

  /** Filter out useless token like Conjunction */
  private def isUseless(token: Token): Boolean =
    token.getBaseForm == "hi" || token.getBaseForm == "hi2" || token.getBaseForm == null

  private def unicodeNormalize = (str: String) => Normalizer.normalize(str, Normalizer.Form.NFKC)

  def tf(document: RawDocument): Map[String, Int] = tf(document.body)

  def tf(document: String): TF =
    tokenize(unicodeNormalize(document))
      .filterNot(isUseless)
      .map(_.getSurfaceForm)
      .groupBy(_.toString).mapValues(_.length)

  def dfs(tfs: List[TF]): DF =
    tfs.flatMap(_.keys).distinct.foldLeft[Map[String, Int]](Map()) { (m, term) =>
      m + (
        term -> tfs.foldLeft[Int](0)((m, tf) => m + (if (tf.isDefinedAt(term)) 1 else 0))
      )
    }

  // n: the number of Documents
  def idf(term: String, dfs: DF, n: Int): Double =
    dfs.get(term).fold[Double](0)(df => scala.math.log(n / df))

  def tfidf(documents: List[RawDocument]): List[DocumentVector] = {
    val tfs: List[(RawDocument, TF)] = documents.map(doc => (doc, tf(doc)))
    val dfMap = dfs(tfs map (_._2))
    println(dfMap)
    tfs.map {
      case (doc, tf) =>
        val tfidf = tf.foldLeft[Map[String, Double]](Map()) { (m, tuple) => // tf value
          val (term: String, tfv: Int) = tuple
          val idfv = idf(term, dfMap, documents.length)
          // m + (term -> tfv * idf(term, dfMap, documents.length))
          println(s"term: $term, tfv: $tfv, idfv:$idfv")
          m + (term -> tfv * idfv)
        }
        DocumentVector(id = doc.id, body = doc.body, tfidf = tfidf)
    }
  }

}

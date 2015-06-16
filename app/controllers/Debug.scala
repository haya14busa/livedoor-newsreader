// scalastyle:off public.methods.have.type for Action in controllers
package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json.Json

class Debug extends Controller {

  def kuromoji(text: String) = Action {
    import org.atilika.kuromoji.Tokenizer
    import org.atilika.kuromoji.Token
    import scala.collection.JavaConversions._
    val tokenizer = Tokenizer.builder.mode(Tokenizer.Mode.NORMAL).build
    Ok(tokenizer.tokenize(text).map { token =>
      s"${token.getSurfaceForm}: ${token.getAllFeatures}"
    }.toString)
  }

  def docvector(text: String) = Action {
    // Ok(logics.DocVector.make(text).toString)
    // Ok(logics.DocVector.wordCount(text).toString)
    Ok(logics.DocVector.tf(text).toString)
  }

  def tfidf = Action {
    import logics.DocVector.RawDocument
    val docs = List(
      RawDocument(1, "すいかとりんごとみかんを食べました．みかんは実際おいしい"),
      RawDocument(2, "りんごとメロンとスイカを買いました"),
      RawDocument(3, "りんごとバナナとナスをもらいました")
    )
    Ok(logics.DocVector.tfidf(docs).toString)
  }

}


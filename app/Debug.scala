import scala.slick.driver.PostgresDriver.simple._
import scala.concurrent.ExecutionContext.Implicits.global
import models.Tables

object Debug {
  def jsoup(guid: Long) = {
    import org.jsoup._
    import collection.JavaConverters._
    val url = s"http://news.livedoor.com/article/detail/$guid/"
    val doc = Jsoup.connect(url).get
    val content = doc.select(".articleBody").asScala.head.text
    content
  }
}

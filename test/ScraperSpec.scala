import org.specs2.mutable._
import org.specs2.runner._

class ScraperSpec extends Specification {

  "Scraper" should {

    ".article" should {
      import logics.Scraper.article

      "scrape livedoor news article" in {
        // http://news.livedoor.com/article/detail/10244767/
        val guid = 10244767
        article(guid) must beSome
      }

      "get article image" in {
        val guid = 10244767
        article(guid) must beSome.which(_._3.isDefined)
      }

      "return None with invalid guid" in {
        // http://news.livedoor.com/article/detail/9999999999/
        val guid: Long = ("9" * 10).toLong // invalid
        article(guid) must beNone
      }

    }
  }
}


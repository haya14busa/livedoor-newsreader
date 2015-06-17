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

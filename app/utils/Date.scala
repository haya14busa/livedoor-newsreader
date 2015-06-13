package utils

object Date {
  import java.text.SimpleDateFormat
  // e.g. <pubDate>Sat, 13 Jun 2015 13:25:09 +0900</pubDate>
  private val RFC2822 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")
  val parseRFC2822: String => java.util.Date = RFC2822.parse
}


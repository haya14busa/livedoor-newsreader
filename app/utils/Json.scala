package utils.json

import play.api.libs.json.{ Json, Writes, JsValue, JsString }

object URL {
  implicit lazy val dateJsonWrites = new Writes[java.net.URL] {
    def writes(url: java.net.URL): JsValue = JsString(url.toString)
  }
}

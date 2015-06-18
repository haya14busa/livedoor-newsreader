import play.api.Play.current
import play.api.db.DB
import slick.jdbc.JdbcBackend.Database

package object globals {
  lazy val db = Database.forDataSource(DB.getDataSource())
}

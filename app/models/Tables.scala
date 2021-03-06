package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{ GetResult => GR }

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Articles.schema ++ PlayEvolutions.schema ++ Relatedocs.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Articles
   *  @param guid Database column guid SqlType(int8), PrimaryKey
   *  @param cgid Database column cgid SqlType(varchar), Length(20,true)
   *  @param title Database column title SqlType(varchar), Length(200,true)
   *  @param description Database column description SqlType(varchar), Length(2000,true)
   *  @param pubdate Database column pubdate SqlType(timestamp)
   *  @param link Database column link SqlType(varchar), Length(200,true)
   *  @param content Database column content SqlType(text)
   *  @param html Database column html SqlType(text)
   *  @param image Database column image SqlType(varchar), Length(200,true), Default(None)
   */
  case class ArticlesRow(guid: Long, cgid: String, title: String, description: String, pubdate: java.sql.Timestamp, link: String, content: String, html: String, image: Option[String] = None)
  /** GetResult implicit for fetching ArticlesRow objects using plain SQL queries */
  implicit def GetResultArticlesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[ArticlesRow] = GR {
    prs =>
      import prs._
      ArticlesRow.tupled((<<[Long], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[String], <<[String], <<[String], <<?[String]))
  }
  /** Table description of table articles. Objects of this class serve as prototypes for rows in queries. */
  class Articles(_tableTag: Tag) extends Table[ArticlesRow](_tableTag, "articles") {
    def * = (guid, cgid, title, description, pubdate, link, content, html, image) <> (ArticlesRow.tupled, ArticlesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(guid), Rep.Some(cgid), Rep.Some(title), Rep.Some(description), Rep.Some(pubdate), Rep.Some(link), Rep.Some(content), Rep.Some(html), image).shaped.<>({ r => import r._; _1.map(_ => ArticlesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column guid SqlType(int8), PrimaryKey */
    val guid: Rep[Long] = column[Long]("guid", O.PrimaryKey)
    /** Database column cgid SqlType(varchar), Length(20,true) */
    val cgid: Rep[String] = column[String]("cgid", O.Length(20, varying = true))
    /** Database column title SqlType(varchar), Length(200,true) */
    val title: Rep[String] = column[String]("title", O.Length(200, varying = true))
    /** Database column description SqlType(varchar), Length(2000,true) */
    val description: Rep[String] = column[String]("description", O.Length(2000, varying = true))
    /** Database column pubdate SqlType(timestamp) */
    val pubdate: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("pubdate")
    /** Database column link SqlType(varchar), Length(200,true) */
    val link: Rep[String] = column[String]("link", O.Length(200, varying = true))
    /** Database column content SqlType(text) */
    val content: Rep[String] = column[String]("content")
    /** Database column html SqlType(text) */
    val html: Rep[String] = column[String]("html")
    /** Database column image SqlType(varchar), Length(200,true), Default(None) */
    val image: Rep[Option[String]] = column[Option[String]]("image", O.Length(200, varying = true), O.Default(None))

    /** Index over (cgid) (database name index_articles_cgid) */
    val index1 = index("index_articles_cgid", cgid)
  }
  /** Collection-like TableQuery object for table Articles */
  lazy val Articles = new TableQuery(tag => new Articles(tag))

  /** Entity class storing rows of table PlayEvolutions
   *  @param id Database column id SqlType(int4), PrimaryKey
   *  @param hash Database column hash SqlType(varchar), Length(255,true)
   *  @param appliedAt Database column applied_at SqlType(timestamp)
   *  @param applyScript Database column apply_script SqlType(text), Default(None)
   *  @param revertScript Database column revert_script SqlType(text), Default(None)
   *  @param state Database column state SqlType(varchar), Length(255,true), Default(None)
   *  @param lastProblem Database column last_problem SqlType(text), Default(None)
   */
  case class PlayEvolutionsRow(id: Int, hash: String, appliedAt: java.sql.Timestamp, applyScript: Option[String] = None, revertScript: Option[String] = None, state: Option[String] = None, lastProblem: Option[String] = None)
  /** GetResult implicit for fetching PlayEvolutionsRow objects using plain SQL queries */
  implicit def GetResultPlayEvolutionsRow(implicit e0: GR[Int], e1: GR[String], e2: GR[java.sql.Timestamp], e3: GR[Option[String]]): GR[PlayEvolutionsRow] = GR {
    prs =>
      import prs._
      PlayEvolutionsRow.tupled((<<[Int], <<[String], <<[java.sql.Timestamp], <<?[String], <<?[String], <<?[String], <<?[String]))
  }
  /** Table description of table play_evolutions. Objects of this class serve as prototypes for rows in queries. */
  class PlayEvolutions(_tableTag: Tag) extends Table[PlayEvolutionsRow](_tableTag, "play_evolutions") {
    def * = (id, hash, appliedAt, applyScript, revertScript, state, lastProblem) <> (PlayEvolutionsRow.tupled, PlayEvolutionsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(hash), Rep.Some(appliedAt), applyScript, revertScript, state, lastProblem).shaped.<>({ r => import r._; _1.map(_ => PlayEvolutionsRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6, _7))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(int4), PrimaryKey */
    val id: Rep[Int] = column[Int]("id", O.PrimaryKey)
    /** Database column hash SqlType(varchar), Length(255,true) */
    val hash: Rep[String] = column[String]("hash", O.Length(255, varying = true))
    /** Database column applied_at SqlType(timestamp) */
    val appliedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("applied_at")
    /** Database column apply_script SqlType(text), Default(None) */
    val applyScript: Rep[Option[String]] = column[Option[String]]("apply_script", O.Default(None))
    /** Database column revert_script SqlType(text), Default(None) */
    val revertScript: Rep[Option[String]] = column[Option[String]]("revert_script", O.Default(None))
    /** Database column state SqlType(varchar), Length(255,true), Default(None) */
    val state: Rep[Option[String]] = column[Option[String]]("state", O.Length(255, varying = true), O.Default(None))
    /** Database column last_problem SqlType(text), Default(None) */
    val lastProblem: Rep[Option[String]] = column[Option[String]]("last_problem", O.Default(None))
  }
  /** Collection-like TableQuery object for table PlayEvolutions */
  lazy val PlayEvolutions = new TableQuery(tag => new PlayEvolutions(tag))

  /** Entity class storing rows of table Relatedocs
   *  @param guid Database column guid SqlType(int8), PrimaryKey
   *  @param rank1 Database column rank1 SqlType(int8), Default(None)
   *  @param rank2 Database column rank2 SqlType(int8), Default(None)
   *  @param rank3 Database column rank3 SqlType(int8), Default(None)
   *  @param rank4 Database column rank4 SqlType(int8), Default(None)
   *  @param rank5 Database column rank5 SqlType(int8), Default(None)
   */
  case class RelatedocsRow(guid: Long, rank1: Option[Long] = None, rank2: Option[Long] = None, rank3: Option[Long] = None, rank4: Option[Long] = None, rank5: Option[Long] = None)
  /** GetResult implicit for fetching RelatedocsRow objects using plain SQL queries */
  implicit def GetResultRelatedocsRow(implicit e0: GR[Long], e1: GR[Option[Long]]): GR[RelatedocsRow] = GR {
    prs =>
      import prs._
      RelatedocsRow.tupled((<<[Long], <<?[Long], <<?[Long], <<?[Long], <<?[Long], <<?[Long]))
  }
  /** Table description of table relatedocs. Objects of this class serve as prototypes for rows in queries. */
  class Relatedocs(_tableTag: Tag) extends Table[RelatedocsRow](_tableTag, "relatedocs") {
    def * = (guid, rank1, rank2, rank3, rank4, rank5) <> (RelatedocsRow.tupled, RelatedocsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(guid), rank1, rank2, rank3, rank4, rank5).shaped.<>({ r => import r._; _1.map(_ => RelatedocsRow.tupled((_1.get, _2, _3, _4, _5, _6))) }, (_: Any) => throw new Exception("Inserting into ? projection not supported."))

    /** Database column guid SqlType(int8), PrimaryKey */
    val guid: Rep[Long] = column[Long]("guid", O.PrimaryKey)
    /** Database column rank1 SqlType(int8), Default(None) */
    val rank1: Rep[Option[Long]] = column[Option[Long]]("rank1", O.Default(None))
    /** Database column rank2 SqlType(int8), Default(None) */
    val rank2: Rep[Option[Long]] = column[Option[Long]]("rank2", O.Default(None))
    /** Database column rank3 SqlType(int8), Default(None) */
    val rank3: Rep[Option[Long]] = column[Option[Long]]("rank3", O.Default(None))
    /** Database column rank4 SqlType(int8), Default(None) */
    val rank4: Rep[Option[Long]] = column[Option[Long]]("rank4", O.Default(None))
    /** Database column rank5 SqlType(int8), Default(None) */
    val rank5: Rep[Option[Long]] = column[Option[Long]]("rank5", O.Default(None))
  }
  /** Collection-like TableQuery object for table Relatedocs */
  lazy val Relatedocs = new TableQuery(tag => new Relatedocs(tag))
}

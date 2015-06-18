name := """livedoor-newsreader"""

version := "1.0-SNAPSHOT"

lazy val root = (
  project in file(".")
  enablePlugins (PlayScala, PhantomJs)
  settings (
    scalacOptions ++= Seq(
      "-Xlint",
      "-Ywarn-unused",
      // "-Ywarn-unused-import", // for ./conf/routes
      "-unchecked", "-deprecation", "-feature",
      "-language:postfixOps",
      "-language:reflectiveCalls",
      "-encoding", "utf8"
    )
  )
)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  specs2 % Test,
  "org.atilika.kuromoji" % "kuromoji" % "0.7.7",
  // Thereare some confliction with this version
  // "org.apache.spark" %% "spark-core" % "1.4.0",
  // "org.apache.spark" %% "spark-mllib" % "1.4.0",
  "org.apache.spark" %% "spark-core" % "1.3.1",
  "org.apache.spark" %% "spark-mllib" % "1.3.1",
  // https://github.com/detro/ghostdriver/issues/397
  // official: "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0" % "test"
  "com.codeborne" % "phantomjsdriver" % "1.2.1" % "test",
  "org.jsoup" % "jsoup" % "1.7.2",
  evolutions,
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
  "com.h2database" % "h2" % "1.4.187", // for test
  "com.typesafe.slick" %% "slick" % "3.0.0",
  "com.typesafe.slick" %% "slick-codegen" % "3.0.0",
  "org.slf4j" % "slf4j-nop" % "1.6.4" // for slick
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
resolvers += "Atilika Open Source repository" at "http://www.atilika.org/nexus/content/repositories/atilika" // for kuromoji

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator

// Disable documentation
sources in (Compile, doc) := Seq.empty

publishArtifact in (Compile, packageDoc) := false

// Create a default Scala style task to run with tests
lazy val testScalastyle = taskKey[Unit]("testScalastyle")

testScalastyle := {
  org.scalastyle.sbt.ScalastylePlugin.scalastyle.in(Test).toTask("").value
}

(test in Test) <<= (test in Test) dependsOn testScalastyle

// Gulp integration
//  - support `controllers.Assets.versioned`?
//    ref: http://d.hatena.ne.jp/nazoking/20141207/1417964951
play.sbt.PlayImport.PlayKeys.playRunHooks += RunSubProcess("gulp watch")

lazy val buildAssetsWithCLI = taskKey[Unit]("Build assets files using CLI tools")

buildAssetsWithCLI := {
  "gulp dist" !
}

// Run buildAssetsWithCLI before `sbt dist`
(packageBin in Universal) <<= (packageBin in Universal) dependsOn buildAssetsWithCLI

// heroku
herokuAppName in Compile := "livedoor-newsreader"

// For PhantomJs
javaOptions in Test ++= PhantomJs.setup(baseDirectory.value)

javaOptions in Test += "-Dconfig.file=conf/test.conf"

slick <<= slickCodeGenTask // register manual sbt command

// sourceGenerators in Compile <+= slickCodeGenTask // register automatic code generation on every compile, remove for only manual use

lazy val slick = TaskKey[Seq[File]]("gen-tables")
lazy val slickCodeGenTask = (sourceManaged, dependencyClasspath in Compile, runner in Compile, streams) map { (dir, cp, r, s) =>
  // val outputDir = (dir / "slick").getPath
  // val outputDir = ((baseDirectory in root) / "app").getAbsolutePath
  val outputDir = "./app" // track output with git for ci & travis...
  val url = "jdbc:postgresql://localhost/livedoornews" // connection info for a pre-populated throw-away, in-memory db for this demo, which is freshly initialized on every run
  val jdbcDriver = "org.postgresql.Driver"
  val slickDriver = "slick.driver.PostgresDriver"
  val pkg = "models"
  toError(r.run("slick.codegen.SourceCodeGenerator", cp.files, Array(slickDriver, jdbcDriver, url, outputDir, pkg), s.log))
  val fname = outputDir + "/models/Tables.scala"
  Seq(file(fname))
}

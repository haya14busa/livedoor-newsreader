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
  "org.apache.spark" %% "spark-core" % "1.4.0",
  // https://github.com/detro/ghostdriver/issues/397
  // official: "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0" % "test"
  "com.codeborne" % "phantomjsdriver" % "1.2.1" % "test"
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

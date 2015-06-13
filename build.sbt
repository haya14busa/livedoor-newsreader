name := """livedoor-newsreader"""

version := "1.0-SNAPSHOT"

lazy val root = (
  project in file(".")
  enablePlugins PlayScala
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
  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

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

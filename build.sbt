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

scalacOptions ++= Seq(
  "-Xlint",
  "-unchecked", "-deprecation", "-feature",
  "-language:postfixOps",
  "-language:reflectiveCalls",
  "-encoding", "utf8"
)

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-jdbc" % "2.4.0", // for sbt evolution
  "com.typesafe.play" %% "play-jdbc-evolutions" % "2.4.0", // for sbt evolution
  "postgresql" % "postgresql" % "9.1-901-1.jdbc4" // for sbt evolution
)

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.0")

// Do not use these default plugins
// // web plugins
// addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.0")
// addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.6")
// addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.3")
// addSbtPlugin("com.typesafe.sbt" % "sbt-rjs" % "1.0.7")
// addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.0")
// addSbtPlugin("com.typesafe.sbt" % "sbt-mocha" % "1.1.0")

// Custom --
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")
addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")
addSbtPlugin("jp.leafytree.sbt" % "sbt-phantomjs" % "0.1.4")

// heroku
// https://devcenter.heroku.com/articles/deploy-scala-and-play-applications-to-heroku-from-travis-ci
resolvers += "Bintray sbt plugin releases" at "http://dl.bintray.com/sbt/sbt-plugin-releases/"
addSbtPlugin("com.heroku" % "sbt-heroku" % "0.4.3")
// /heroku

resolvers += "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/" // for scalastyle

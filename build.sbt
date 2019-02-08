name := """image browse service"""
organization := "com.hirokikana"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.1" % Test
libraryDependencies ++= Seq(
    "net.debasishg" %% "redisclient" % "3.9"
)
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.hirokikana.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.hirokikana.binders._"

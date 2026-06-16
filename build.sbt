name := "play-elastic-search"

version := "1.0.0"

scalaVersion := "3.8.4"

libraryDependencies += guice
libraryDependencies += "co.elastic.clients" % "elasticsearch-java" % "9.4.2"
libraryDependencies += "org.projectlombok" % "lombok" % "1.18.46" % "provided"

Compile / javacOptions ++= Seq("-proc:full")

lazy val root = (project in file(".")).enablePlugins(PlayJava)
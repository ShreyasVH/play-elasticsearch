name := "play-elastic-search"

version := "1.0.0"

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "co.elastic.clients" % "elasticsearch-java" % "8.5.2"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.11.4"
libraryDependencies += "org.projectlombok" % "lombok" % "1.18.24" % "provided"



lazy val root = (project in file(".")).enablePlugins(PlayJava)
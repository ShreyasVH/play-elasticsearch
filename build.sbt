name := "play-elastic-search"

version := "1.0.0"

scalaVersion := "3.8.4"

libraryDependencies += guice
libraryDependencies += "co.elastic.clients" % "elasticsearch-java" % "9.4.2"
libraryDependencies += "org.projectlombok" % "lombok" % "1.18.46" % "provided"

val pekkoVersion = "1.6.0"

// Pekko dependencies used by Play
libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-actor"                 % pekkoVersion,
  "org.apache.pekko" %% "pekko-actor-typed"           % pekkoVersion,
  "org.apache.pekko" %% "pekko-stream"                % pekkoVersion,
  "org.apache.pekko" %% "pekko-slf4j"                 % pekkoVersion,
  "org.apache.pekko" %% "pekko-serialization-jackson" % pekkoVersion
)

Compile / javacOptions ++= Seq("-proc:full")

lazy val root = (project in file(".")).enablePlugins(PlayJava)
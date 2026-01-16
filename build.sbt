name := "play-elastic-search"

version := "1.0.0"

scalaVersion := "2.13.18"

libraryDependencies += guice
libraryDependencies += "co.elastic.clients" % "elasticsearch-java" % "9.2.3"
//libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.14.0"
libraryDependencies += "org.projectlombok" % "lombok" % "1.18.42" % "provided"

val jacksonV = "2.14.3"

dependencyOverrides ++= Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonV,
  "com.fasterxml.jackson.core" % "jackson-core" % jacksonV,
  "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonV,
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonV,
  "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonV,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jdk8" % jacksonV,
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonV,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-cbor" % jacksonV
)

Compile / javacOptions ++= Seq("-proc:full")

lazy val root = (project in file(".")).enablePlugins(PlayJava)
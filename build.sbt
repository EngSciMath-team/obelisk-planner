name := "obelisk-planner"

version := "0.1"

scalaVersion := "2.12.8"

// clp-java: CLP solver
libraryDependencies += "com.quantego" % "clp-java" % "1.16.10"
// jackson-module-scala, for JSON parsing
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.8"

// For testing
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % Test


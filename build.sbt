import Dependencies._

lazy val stanz = (project in file("."))
  .settings(
    name                := "stanz",
    organization        := "com.github.sdual.stanz",
    scalaVersion        := "2.12.8",
    version             := "0.1.0-SNAPSHOT",
    libraryDependencies ++= stanzDependencies,
  )

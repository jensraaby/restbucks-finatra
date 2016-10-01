import sbt.Keys._


name := "restbucks-finatra"
version := "0.0.1"
organization := "com.jensraaby"

scalaVersion := "2.11.8"

fork := true // required for javaOptions to be passed
parallelExecution in ThisBuild := false

resolvers ++= Seq(
    Resolver.sonatypeRepo("releases"),
"Twitter Maven" at "https://maven.twttr.com"
)

// Make test output quiet unless a test fails:
// see http://www.scalatest.org/user_guide/using_the_runner
testOptions in Test += Tests.Argument("-oCOLHPQ")
javaOptions in Test += "-Dlogback.configurationFile=./src/test/resources/logback-test.xml"

enablePlugins(JavaAppPackaging)

assemblyMergeStrategy in assembly := {
  case "BUILD" => MergeStrategy.discard
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case other => MergeStrategy.defaultMergeStrategy(other)
}

excludeFilter in (Compile, unmanagedSources) := HiddenFileFilter || "BUILD"
excludeFilter in (Compile, unmanagedResources) := HiddenFileFilter || "BUILD"

unmanagedResourceDirectories in Compile += baseDirectory.value / "src" / "main" / "webapp"

Revolver.settings


lazy val versions = new {
  val finatra = "2.4.0"
  val guice = "4.0"
  val logback = "1.1.7"
  val mockito = "1.9.5"
  val scalatest = "2.2.6"
  val specs2 = "2.3.12"
  val circe = "0.5.1"
}

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % versions.finatra,
  "com.twitter" %% "finatra-httpclient" % versions.finatra,
  "ch.qos.logback" % "logback-classic" % versions.logback,

  "com.twitter" %% "finatra-http" % versions.finatra % "test",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter" %% "inject-server" % versions.finatra % "test",
  "com.twitter" %% "inject-app" % versions.finatra % "test",
  "com.twitter" %% "inject-core" % versions.finatra % "test",
  "com.twitter" %% "inject-modules" % versions.finatra % "test",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",

  "org.mockito" % "mockito-core" % versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2" % versions.specs2 % "test")

// custom dependencies (ie. non-Finatra)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % versions.circe,
  "io.circe" %% "circe-generic" % versions.circe,
  "io.circe" %% "circe-parser" % versions.circe
)


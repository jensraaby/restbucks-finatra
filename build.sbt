enablePlugins(JavaAppPackaging)

name := "restbucks-finatra"
version := scala.util.Properties.envOrElse("BUILD_NUMBER","dev")
organization := "com.jensraaby"

scalaVersion := "2.11.8"
fork := true
parallelExecution in ThisBuild := false

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

// Make test output quiet unless a test fails:
// see http://www.scalatest.org/user_guide/using_the_runner
testOptions in Test += Tests.Argument("-oCOLHPQ")
javaOptions in Test += "-Dlogback.configurationFile=./src/test/resources/logback-test.xml"

lazy val versions = new {
  val finatra = "2.2.0"
  val logbackClassic = "1.1.3"
  val mockito = "1.9.5"
  val scalatest = "2.2.6"
  val specs2 = "2.3.12"
  val guice = "4.0"
  val circe = "0.4.1"
  val junit = "4.11"
}

assemblyMergeStrategy in assembly := {
  case "BUILD" => MergeStrategy.discard
  case other => MergeStrategy.defaultMergeStrategy(other)
}

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % versions.finatra,
  "com.twitter" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "finatra-httpclient" % versions.finatra,
  "com.twitter" %% "finatra-httpclient" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "finatra-slf4j" % versions.finatra,
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test",
  "com.twitter" %% "finatra-jackson" % versions.finatra % "test" classifier "tests",

  "com.twitter" %% "inject-app" % versions.finatra % "test",
  "com.twitter" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % "test",
  "com.twitter" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % "test",
  "com.twitter" %% "inject-modules" % versions.finatra % "test" classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % "test",
  "com.twitter" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",

  "ch.qos.logback" % "logback-classic" % versions.logbackClassic,

  "org.mockito" % "mockito-core" %  versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
//  "org.specs2" %% "specs2" % versions.specs2 % "test",
  "junit" % "junit" % versions.junit % "test",

  "io.circe" %% "circe-core" % versions.circe,
  "io.circe" %% "circe-generic" % versions.circe,
  "io.circe" %% "circe-parser" % versions.circe
  )

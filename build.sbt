name := "restbucks-finatra"
organization := "com.jensraaby"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.8"

parallelExecution in ThisBuild := false
fork := true // required for javaOptions to be passed

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

// Make test output quiet unless a test fails:
// see http://www.scalatest.org/user_guide/using_the_runner
testOptions in Test += Tests.Argument("-oCOLHPQ")
javaOptions in Test += "-Dlogback.configurationFile=./src/test/resources/logback-test.xml"

javaOptions ++= Seq(
  "-Dlog.service.output=/dev/stdout",
  "-Dlog.access.output=/dev/stdout"
)

enablePlugins(JavaAppPackaging)

assemblyMergeStrategy in assembly := {
  case "BUILD" => MergeStrategy.discard
  case "META-INF/io.netty.versions.properties" => MergeStrategy.last
  case other => MergeStrategy.defaultMergeStrategy(other)
}

unmanagedResourceDirectories in Compile += baseDirectory.value / "src" / "main" / "webapp"

Revolver.settings


lazy val versions = new {
  val finatra = "2.8.0"
  val guice = "4.0"
  val logback = "1.1.7"
  val mockito = "1.9.5"
  val scalatest = "3.0.1"
  val scalacheck = "1.13.4"
  val specs2 = "2.4.17"

  val circe = "0.5.1"
  val metricsCore = "3.1.2"
  val finagleMetrics = "0.0.3"
}

libraryDependencies ++= Seq(
  "com.twitter" %% "finatra-http" % versions.finatra,
  "com.twitter" %% "finatra-httpclient" % versions.finatra,
  "ch.qos.logback" % "logback-classic" % versions.logback,

  "com.twitter" %% "finatra-http" % versions.finatra % Test,
  "com.twitter" %% "finatra-jackson" % versions.finatra % Test,
  "com.twitter" %% "inject-server" % versions.finatra % Test,
  "com.twitter" %% "inject-app" % versions.finatra % Test,
  "com.twitter" %% "inject-core" % versions.finatra % Test,
  "com.twitter" %% "inject-modules" % versions.finatra % Test,
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % Test,

  "com.twitter" %% "finatra-http" % versions.finatra % Test classifier "tests",
  "com.twitter" %% "finatra-jackson" % versions.finatra % Test classifier "tests",
  "com.twitter" %% "inject-server" % versions.finatra % Test classifier "tests",
  "com.twitter" %% "inject-app" % versions.finatra % Test classifier "tests",
  "com.twitter" %% "inject-core" % versions.finatra % Test classifier "tests",
  "com.twitter" %% "inject-modules" % versions.finatra % Test classifier "tests",

  "org.mockito" % "mockito-core" % versions.mockito % Test,
  "org.scalacheck" %% "scalacheck" % versions.scalacheck % Test,
  "org.scalatest" %% "scalatest" % versions.scalatest % Test,
  "org.specs2" %% "specs2-mock" % versions.specs2 % Test)

// custom dependencies (ie. non-Finatra)
libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % versions.circe,
  "io.circe" %% "circe-generic" % versions.circe,
  "io.circe" %% "circe-parser" % versions.circe,

  "com.github.rlazoti" %% "finagle-metrics" % versions.finagleMetrics,
  "io.dropwizard.metrics" % "metrics-core" % versions.metricsCore,
  "io.dropwizard.metrics" % "metrics-graphite" % versions.metricsCore
)

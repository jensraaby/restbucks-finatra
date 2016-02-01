name := "restbucks-finatra"
version := scala.util.Properties.envOrElse("BUILD_VERSION","dev")
organization := "jensraaby"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

// Make test output quiet unless a test fails:
// see http://www.scalatest.org/user_guide/using_the_runner
testOptions in Test += Tests.Argument("-oCOLHPQ")
javaOptions in Test += "-Dlogback.configurationFile=./src/test/resources/logback-test.xml"
fork in Test := true

lazy val versions = new {
  val finatra = "2.1.2"
  val logbackClassic = "1.1.3"
  val mockito = "1.9.5"
  val scalatest = "2.2.4"
  val specs2 = "2.3.12"
  val guice = "4.0"
}

libraryDependencies ++= Seq(
  "com.twitter.finatra" %% "finatra-http" % versions.finatra,
  "com.twitter.finatra" %% "finatra-http" % versions.finatra % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-httpclient" % versions.finatra,
  "com.twitter.finatra" %% "finatra-httpclient" % versions.finatra % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-slf4j" % versions.finatra,

  "com.twitter.inject" %% "inject-app" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-app" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-core" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-modules" % versions.finatra % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test",
  "com.twitter.inject" %% "inject-server" % versions.finatra % "test" classifier "tests",
  "com.google.inject.extensions" % "guice-testlib" % versions.guice % "test",


  "ch.qos.logback" % "logback-classic" % versions.logbackClassic,

  "org.mockito" % "mockito-core" %  versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2" % versions.specs2 % "test"
)

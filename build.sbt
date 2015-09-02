name := "restbucks-finatra"
version := scala.util.Properties.envOrElse("BUILD_VERSION","dev")
organization := "jensraaby"

scalaVersion := "2.11.7"

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com",
  "Finatra Repo" at "http://twitter.github.com/finatra"
)

lazy val finatraVersion = "2.0.0.RC1"
lazy val finagleVersion = "6.28.0"

libraryDependencies ++= Seq(
  "com.twitter.finatra" %% "finatra-http" % finatraVersion,
  "com.twitter.finatra" %% "finatra-http" % finatraVersion % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-httpclient" % finatraVersion,
  "com.twitter.finatra" %% "finatra-httpclient" % finatraVersion % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-slf4j" % finatraVersion,

  "com.twitter.inject" %% "inject-app" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-core" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-modules" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-server" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-app" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % finatraVersion % "test" classifier "tests",

  "ch.qos.logback" % "logback-classic" % "1.1.3",

  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)


# restbucks-finatra
An attempt to learn to use the Finatra web framework for something

## Steps
I'm documenting the steps as I go, because Finatra 2 is slightly lacking in this
regard and it's a useful reference for myself.

### Create build.sbt
Finatra 2 has a number of dependencies, and it's useful to set a few options in
the SBT build file for packaging. This is a barebones setup as of August 2015:

```
name := "restbucks-finatra"
version := scala.util.Properties.envOrElse("BUILD_VERSION","dev")
organization := "jensraaby"
version := "0.1"

scalaVersion := "2.11.7"


resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com",
  "Finatra Repo" at "http://twitter.github.com/finatra"
)

lazy val finatraVersion = "2.0.0.M2"
lazy val finagleVersion = "6.25.0"

libraryDependencies ++= Seq(
  "com.twitter.finatra" %% "finatra-http" % finatraVersion,
  "com.twitter.finatra" %% "finatra-http" % finatraVersion % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-httpclient" % finatraVersion,
  "com.twitter.finatra" %% "finatra-httpclient" % finatraVersion % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-logback" % finatraVersion,

  "com.twitter.inject" %% "inject-app" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-core" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-modules" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-server" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-app" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % finatraVersion % "test" classifier "tests",

  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.specs2" %% "specs2" % "2.3.12" % "test"
)

```
I haven't quite worked out why they import Specs2 as well as Scalatest for the
examples, but I think it may have something to do with Specs2 depending on JUnit.

At this point, it should be possible to load SBT up.

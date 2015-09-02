# restbucks-finatra
An attempt to learn to use the Finatra web framework for something.

As most documentation for doing non-trivial things with Finatra is either
non-existent or a reference to some example code, this is an attempt to go
through the motions and make notes.

The aim is to get the following working:
* Scala Guice dependency injection
* The HttpClient module for contacting remote services
* Acceptance tests using the EmbeddedHttpServer
* Mockito for isolating components
* InMemoryHttpService for mocking remote HTTP services during tests
* TypeSafe Config for environment-specific configuration

## Steps
I'm documenting the steps as I go, because Finatra 2 is slightly lacking in this
regard and it's a useful reference for myself.

At this point, all you need is Java 8 installed.
### Create build.sbt
Finatra 2 has a number of dependencies, and it's useful to set a few options in
the SBT build file for packaging and running.

I've allowed setting the version dynamically, which can be rather useful when you execute the
build in CI, but most people would probably choose a version number like "0.1".

This is a barebones setup as of August 2015:

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

At this point, it should be possible to load SBT up and resolve dependencies.

The next bit is to set the SBT version, which forces the version specified. This
goes in "project/build.properties":
```
sbt.version=0.13.9
```
Note that versions of SBT prior to 0.13.7 required blank lines between every
statement in build.sbt, which is why you might see lots of spaced-out files.

### Basic Finatra server

We need a sources root for our code.

``` mkdir -p src/main/scala```

For tests:

```mkdir -p src/test/scala```

I'm going to use "jensraaby.restbucks" as my package for this project, so I need
another couple of directories.

```
mkdir -p src/main/scala/jensraaby/restbucks
```

Now I can actually create a dummy server.
We just need a class which extends ```com.twitter.http.HttpServer``` and
overrides the ```configureHttp``` method to add some controllers. This part of
setup is actually quite well covered by the Readme file
[Finatra on Github](https://github.com/twitter/finatra).

Controllers are just classes which extend ```com.twitter.finatra.http.Controller```,
This class allows you to call methods like this:

```
get("/blah") { request: Request =>
  "Blah blah blah"
}
```
Similarly, there are post, put etc. methods for the HTTP verbs.

What's interesting here is the type signature of ```get```.

```
def get[RequestType, ResponseType](route: String)(callback: (RequestType) ⇒
ResponseType)(implicit arg0: Manifest[RequestType], arg1:
Manifest[ResponseType]): Unit
```

We can ignore the implicits for the time being, as they are more to do with
ensuring the types can be correctly interpreted for HTTP. 

Note that ```get``` essentially maps an HTTP verb (GET) and route (e.g. "/hello") to a
handler/callback. If you want to unit test the callback, you can extract it to
its own function so you don't need a server running.

For example, use something like this:

```
class SomeController extends Controller {
  val handleHelloRequest = { req: Request =>
    Future.value("Hello")
  }

  get("/hello")(handleHelloRequest)
}
```

Note I am not defining a method on the class, but a function. This means I can
just pass it to the "get" method directly rather than have to fiddle with
passing arguments.


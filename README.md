# restbucks-finatra
An attempt to learn to use the Finatra web framework for something.

As most documentation for doing non-trivial things with Finatra 2 is hard to find (or typically a reference to some example code), this is an attempt to go through the motions and make notes.

The aim is to get the following working:
* Scala Guice dependency injection
* The HttpClient module for contacting remote services
* Acceptance tests using the EmbeddedHttpServer
* Mockito for isolating components for unit tests
* InMemoryHttpService for mocking remote HTTP services during tests

## Steps
I'm documenting the steps as I go, because Finatra 2 as it's a useful reference for myself.

At this point, all you need is Java 7 or Java 8 installed.

### Create build.sbt
Finatra 2 has a number of dependencies, and it's useful to set a few options in
the SBT build file for packaging and running.

I've allowed setting the version dynamically, which can be rather useful when you execute the
build in CI, but most people would probably choose a version number like "0.1".

This is a barebones setup as of September 2015:

```
name := "restbucks-finatra"
version := scala.util.Properties.envOrElse("BUILD_VERSION","dev")
organization := "jensraaby"

scalaVersion := "2.11.7"

// Make test output quiet unless a test fails:
// see http://www.scalatest.org/user_guide/using_the_runner
testOptions in Test += Tests.Argument("-oCOLHPQ")

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  "Twitter Maven" at "https://maven.twttr.com"
)

lazy val versions = new {
  val finatra = "2.0.1"
  val logbackClassic = "1.1.3"
  val mockito = "1.9.5"
  val scalatest = "2.2.4"
  val specs2 = "2.3.12"
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

  "ch.qos.logback" % "logback-classic" % versions.logbackClassic,

  "org.mockito" % "mockito-core" %  versions.mockito % "test",
  "org.scalatest" %% "scalatest" % versions.scalatest % "test",
  "org.specs2" %% "specs2" % versions.specs2 % "test"
)


```
I haven't quite worked out why they import Specs2 as well as Scalatest for the
examples, but if you remove it you get this error:
```
[warn] Class org.junit.runner.RunWith not found - continuing with a stub.
```

At this point, it should be possible to load SBT up and resolve dependencies.

The next part is to set the SBT version, which forces the version specified. This
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

I'm going to use "com.jensraaby.restbucks" as my package for this project, so I need
another couple of directories.

```
mkdir -p src/main/scala/com/jensraaby/restbucks
```

Now I can actually create a dummy server.
We just need a class which extends ```com.twitter.http.HttpServer``` and
overrides the ```configureHttp``` method to add some controllers. This part of
setup is quite well covered by the Readme file
[Finatra on Github](https://github.com/twitter/finatra).

Controllers are just classes which extend ```com.twitter.finatra.http.Controller```,
This class allows you to call methods like this:

```
get("/blah") { request: Request =>
  "Blah blah blah"
}
```
Similarly, there are `post`, `put` etc. methods for the HTTP verbs.

What's interesting here is the type signature of ```get```.

```
def get[RequestType, ResponseType](route: String)(callback: (RequestType) ⇒
ResponseType)(implicit arg0: Manifest[RequestType], arg1:
Manifest[ResponseType]): Unit
```

We can ignore the implicits for the time being, as they are more to do with
ensuring the types can be correctly wrapped/converted for HTTP.

Note that ```get``` essentially maps an HTTP verb (GET) and route (e.g. "/hello") to a
handler function (callback). If you want to unit test the callback, you can extract it to
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

### Aside: Making test output quieter
When you are developing in a test driven way, you will be executing the tests very frequently.
It's very annoying if the test runner vomits a lot of unnecessary information into your console, so I've used a few tricks for this project.

We are using logback for logging, so there is a (src/test/resources/logback-test.xml) file:
```
<configuration>
    <!-- Console Appender -->
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%msg%n</pattern>
        </encoder>
    </appender>

    <!-- Per Package Config -->
    <logger name="com.jensraaby" level="ERROR"/>
    <logger name="com.twitter.app" level="ERROR"/>

    <!-- Root Logger -->
    <root level="warn">
        <appender-ref ref="STDOUT"/>
    </root>
</configuration>
```
The per-package config for com.twitter.app will remove some of the noise when running FeatureTest classes.
The other step in removing the statistics and other information from your terminal is to set the verbose flag. For example:
```
  override val server = new EmbeddedHttpServer(new RestbucksServer, verbose = false)

```

Finally, in the build.sbt file, I've added 3 magical lines:
```
testOptions in Test += Tests.Argument("-oCOLHPQ")
javaOptions in Test += "-Dlogback.configurationFile=./src/test/resources/logback-test.xml"
fork in Test := true
```

With this configuration, running `test` from inside sbt gives only the following output:
```
> test
[info] ScalaTest
[info] Run completed in 4 seconds, 622 milliseconds.
[info] Total number of tests run: 2
[info] Suites: completed 2, aborted 0
[info] Tests: succeeded 2, failed 0, canceled 0, ignored 0, pending 0
[info] All tests passed.
[info] Passed: Total 2, Failed 0, Errors 0, Passed 2
[success] Total time: 5 s, completed 23-Sep-2015 21:57:15
```

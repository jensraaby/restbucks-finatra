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
I'm documenting the steps as I go, because Finatra 2 is shiny, and it's a useful reference for myself (and maybe you, if you're reading this!).

At this point, all you need is Java 7 or Java 8 installed.


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

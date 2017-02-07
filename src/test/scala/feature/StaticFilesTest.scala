package feature

import com.jensraaby.restbucks.RestbucksServer
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

import scala.io.Source

class StaticFilesTest extends FeatureTest {
  override val server: EmbeddedHttpServer = new EmbeddedHttpServer(new RestbucksServer, verbose = false, disableTestLogging = true)

  test("index route returns hello world page") {
    server.httpGet(
      path = "/",
      andExpect = Status.Ok,
      withBody = Source.fromFile("./src/main/resources/index.html").mkString
    )
  }

  test("javascript file") {
    val response = server.httpGet("/js/jquery.js",
      andExpect = Status.Ok)

    response.contentString should startWith("{")
  }

  test("CSS file") {
    server.httpGet("/css/restbucks.css",
      andExpect = Status.Ok)
  }
}

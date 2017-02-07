package feature

import com.google.inject.Stage
import com.jensraaby.restbucks.RestbucksServer
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class CorsHeaderFeatureTest extends FeatureTest {
  val server = new EmbeddedHttpServer(
    stage = Stage.PRODUCTION,
    twitterServer = new RestbucksServer,
    verbose = false,
    disableTestLogging = true)

    test("startup") {
      server.assertStarted()
    }

    test("add CORS header") {
      val response = server.httpGet("/index.html",
        headers = Map("Origin" -> "myOrigin",
                      "Access-Control-Request-Method" -> "GET"),
        andExpect = Status.Ok)

      response.headerMap.get("Access-Control-Allow-Origin") shouldBe Some("myOrigin")
//      response.headerMap.get("Access-Control-Allow-Headers") shouldBe Some("Origin, X-Requested-With, Content-Type, Accept")
      response.headerMap.get("Access-Control-Allow-Credentials") shouldBe Some("true")
    }

    test("add CORS headers when requested") {
      val response = server.httpGet("/index.html",
        headers = Map("Origin" -> "myOrigin",
          "Access-Control-Request-Method" -> "GET",
          "Access-Control-Request-Headers" -> "X-Custom-Header"),
        andExpect = Status.Ok)

      response.headerMap.get("Access-Control-Allow-Origin") shouldBe Some("myOrigin")
      response.headerMap.get("Access-Control-Allow-Credentials") shouldBe Some("true")
      response.headerMap.get("Access-Control-Allow-Headers") shouldBe None //Some("X-Custom-Header")
    }
}

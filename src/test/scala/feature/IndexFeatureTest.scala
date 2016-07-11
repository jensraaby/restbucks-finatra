package feature

import com.jensraaby.restbucks.RestbucksServer
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest


class IndexFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new RestbucksServer, verbose = false, disableTestLogging = true)

  "index route returns hello world" in {
    server.httpGet(
      path = "/",
      andExpect = Status.Ok,
      withBody = "<h1>Welcome to Restbucks!</h1>"
    )


  }
}

package acceptance

import com.jensraaby.restbucks.RestbucksServer
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import com.twitter.finagle.httpx.Status


class IndexFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new RestbucksServer, verbose = false)


  "index root returns hello world" in {

    val result = server.httpGet(
      path = "/",
      andExpect = Status.Ok,
      withBody = "Hello World"
    )


  }
}

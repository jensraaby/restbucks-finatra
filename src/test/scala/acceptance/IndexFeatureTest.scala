package acceptance

import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import jensraaby.restbucks.RestbucksServer
import com.twitter.finagle.httpx.Status


class IndexFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new RestbucksServer)


  "index root returns hello world" in {

    val result = server.httpGet(
      path = "/",
      andExpect = Status.Ok,
      withBody = "Hello World"
    )


  }
}

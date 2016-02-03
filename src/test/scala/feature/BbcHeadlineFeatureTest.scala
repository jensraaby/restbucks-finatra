package feature

import com.jensraaby.restbucks.RestbucksServer
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class BbcHeadlineFeatureTest extends FeatureTest {
  override protected val server = new EmbeddedHttpServer(new RestbucksServer)

  "returns headlines from BBC news in JSON" in {
    server.httpGet(path = "/bbc",
      andExpect = Status.Ok,
      withBody =
        """{"top_story":{"headline":"Something happened"}}""")
  }
}

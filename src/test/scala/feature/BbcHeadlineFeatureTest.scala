package feature

import com.google.inject.testing.fieldbinder.Bind
import com.jensraaby.restbucks.RestbucksServer
import com.jensraaby.restbucks.modules.BBCWebHttpClient
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Response, Request, Status}
import com.twitter.finatra.http.test.{HttpMockResponses, EmbeddedHttpServer}
import com.twitter.finatra.httpclient.test.InMemoryHttpService
import com.twitter.inject.server.FeatureTest

class BbcHeadlineFeatureTest extends FeatureTest with HttpMockResponses {

  val mockBBC = new InMemoryHttpService
  @Bind @BBCWebHttpClient val fakeBBCSite: Service[Request, Response] = mockBBC
  override protected val server = new EmbeddedHttpServer(new RestbucksServer)

  "returns headlines from BBC news in JSON" in {
    mockBBC.mockGet("/news", andReturn = response(200).body("<this>"), sticky = false)
    server.httpGet(path = "/bbc",
      andExpect = Status.Ok,
      withJsonBody =
        """
          {
           "top_story":
             {
               "headline":"Something happened: <this>"
             }
          }
          """)
  }
}

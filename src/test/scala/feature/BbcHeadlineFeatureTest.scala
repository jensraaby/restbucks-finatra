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

  val headline = "Something happened"
  val link = "http://somewhere"
  val bbcNewsHtml =
    s"""
      |<div id="comp-top-story-1">
      | <a href="$link">
      |   <h3><span>$headline</span></h3>
      | </a>
      |</div>
    """.stripMargin


  val mockBBC = new InMemoryHttpService
  @Bind @BBCWebHttpClient val fakeBBCSite: Service[Request, Response] = mockBBC
  override protected val server = new EmbeddedHttpServer(new RestbucksServer)

  "returns headlines from BBC news in JSON" in {
    mockBBC.mockGet(
      path = "/news",
      andReturn = response(200).body(bbcNewsHtml),
      sticky = false)

    server.httpGet(path = "/bbc",
      andExpect = Status.Ok,
      withJsonBody =
        s"""
          {
           "top_story":
             {
               "headline":"$headline",
               "link": "$link"
             }
          }
          """)
  }
}

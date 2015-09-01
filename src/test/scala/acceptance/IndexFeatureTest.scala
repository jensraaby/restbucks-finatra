package acceptance

import com.twitter.finagle.netty3.Ok
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest
import jensraaby.restbucks.RestbucksServer
import org.jboss.netty.handler.codec.http.HttpResponseStatus
import org.scalatest.FunSpec


class IndexFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new RestbucksServer)


  "index root returns hello world" in {

    val result = server.httpGet(
      path = "/",
      andExpect = HttpResponseStatus.OK,
      withBody = "Hello World"
    )


  }
}

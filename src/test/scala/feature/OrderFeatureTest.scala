package feature

import com.jensraaby.restbucks.RestbucksServer
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.test.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

/**
  * Created by jens on 04/02/2016.
  */
class OrderFeatureTest extends FeatureTest {

  val server = new EmbeddedHttpServer(new RestbucksServer)

  "Restbucks takes an order in JSON" in {
    server.httpPost(path = "/order",
      postBody =
        """
          |
          | {
          |   "location": "takeAway",
          |   "item" : {
          |     "name": "latte",
          |     "quantity": 1,
          |     "milk": "whole",
          |     "size": "small"
          |   }
          |}
        """.stripMargin,
      andExpect = Status.Ok,
      withJsonBody =
        """
          |{
          |   "location": "takeAway",
          |   "items": [{
          |     "name": "latte",
          |     "quantity": 1,
          |     "milk": "whole",
          |     "size": "small"
          |   }],
          |   "cost": 3.00,
          |   "currency": "GBP",
          |   "link_payment": "/order/1234/payment"
          |}
        """.stripMargin)
  }

}

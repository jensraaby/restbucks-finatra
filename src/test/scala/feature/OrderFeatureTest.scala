package feature

import com.jensraaby.restbucks.RestbucksServer
import com.twitter.finagle.http.Status
import com.twitter.finatra.http.EmbeddedHttpServer
import com.twitter.inject.server.FeatureTest

class OrderFeatureTest extends FeatureTest {

  override val server = new EmbeddedHttpServer(new RestbucksServer, disableTestLogging = true)

  test("Restbucks takes an order for a latte") {
    server.httpPost(path = "/order",
      postBody =
        """
          |
          | {
          |   "location": "takeAway",
          |   "items" : [{
          |     "name": "latte",
          |     "quantity": 1,
          |     "milk": "whole",
          |     "size": "small"
          |   }]
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

  test("Restbucks takes an order for 2 lattes") {
    server.httpPost(path = "/order",
      postBody =
        """
          |
          | {
          |   "location": "takeAway",
          |   "items" : [{
          |     "name": "latte",
          |     "quantity": 2,
          |     "milk": "whole",
          |     "size": "small"
          |   }]
          |}
        """.stripMargin,
      andExpect = Status.Ok,
      withJsonBody =
        """
          |{
          |   "location": "takeAway",
          |   "items": [{
          |     "name": "latte",
          |     "quantity": 2,
          |     "milk": "whole",
          |     "size": "small"
          |   }],
          |   "cost": 6.00,
          |   "currency": "GBP",
          |   "link_payment": "/order/1234/payment"
          |}
        """.stripMargin)
  }

  test("Simple wrapped value") {
    server.httpGet("/orders/1",
      andExpect = Status.Ok,
      withJsonBody =
        """
          |{
          |  "id": "1"
          |}
        """.stripMargin)
  }
}

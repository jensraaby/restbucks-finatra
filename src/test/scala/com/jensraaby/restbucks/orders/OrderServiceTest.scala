package com.jensraaby.restbucks.orders

import com.twitter.util.Await
import org.scalatest.{FlatSpec, Matchers}


class OrderServiceTest extends FlatSpec with Matchers {

  "An order service" should "create an order with the correct price" in {
    val service = new OrderService(new StandardProductProvider)
    val order = Order("takeaway", Seq(OrderItem("latte", 1, "whole", "small")))

    val acknowledgedOrder = Await.result(service.create(order))
    acknowledgedOrder.cost shouldBe 3.0
  }

  it should "not return an order if the item does not exist" in {
      val service = new OrderService(new StandardProductProvider)
      val order = Order("takeaway", Seq(OrderItem("caffelungo", 1, "skimmed", "tiny")))

      intercept[RuntimeException](Await.result(service.create(order))).getMessage shouldBe "invalid order"
  }
}

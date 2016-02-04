package com.jensraaby.restbucks.controllers

import com.twitter.finatra.http.Controller

case class OrderItem(name: String, quantity: Int, milk: String, size: String)
case class Order(location: String, item: OrderItem)
case class AcknowledgedOrder(location: String, items: Seq[OrderItem], cost: BigDecimal, currency: String, linkPayment: String)


class OrderController extends Controller {


  post("/order") { order: Order =>
    createOrder(order)
  }


  def createOrder(order: Order): AcknowledgedOrder = {
    AcknowledgedOrder(order.location, Seq(order.item), 3.00, "GBP", "/order/1234/payment")
  }
}

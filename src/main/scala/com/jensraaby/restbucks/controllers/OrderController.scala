package com.jensraaby.restbucks.controllers

import com.jensraaby.restbucks.orders.{AcknowledgedOrder, Order, OrderItem}
import com.twitter.finatra.http.Controller


class OrderController extends Controller {

  post("/order") { order: Order =>
    createOrder(order)
  }


  def createOrder(order: Order): AcknowledgedOrder = {
    val itemPrice = 3.00
    val price = order.items.foldLeft(itemPrice) { (p, item) =>
      item.quantity * p
    }
    AcknowledgedOrder(order.location, order.items, price, "GBP", "/order/1234/payment")
  }
}

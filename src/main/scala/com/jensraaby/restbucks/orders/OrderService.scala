package com.jensraaby.restbucks.orders

import com.twitter.util.Future


class OrderService {
  def create(order: Order): Future[AcknowledgedOrder] = {
    val itemPrice = 3.00
    val price = order.items.foldLeft(itemPrice) { (p, item) =>
      item.quantity * p
    }
    Future.value(AcknowledgedOrder(order.location, order.items, price, "GBP", "/order/1234/payment"))
  }

}

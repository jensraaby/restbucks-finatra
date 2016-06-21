package com.jensraaby.restbucks.orders

import javax.inject.Inject

import com.twitter.util.Future


class OrderService @Inject()(productProvider: Inventory) {
  def create(order: Order): Future[AcknowledgedOrder] =
    if (validateOrder(order)) {
      val totalPrice = order.items.map(calculateItemPrice).sum
      Future.value(AcknowledgedOrder(order.location, order.items, totalPrice, "GBP", "/order/1234/payment"))
    } else {
      Future.exception(new RuntimeException("invalid order"))
    }

  private def calculateItemPrice(item: OrderItem): Double = {
    productProvider.prices(item.name) * item.quantity
  }

  private def validateOrder(order: Order) =
    order.items.map(item => productProvider.products.contains(item.name))
      .foldLeft(true)(_ && _)
}

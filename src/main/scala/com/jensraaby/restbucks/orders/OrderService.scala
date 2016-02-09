package com.jensraaby.restbucks.orders

import javax.inject.Inject

import com.google.inject.ImplementedBy
import com.twitter.util.Future

@ImplementedBy(classOf[StandardProductProvider])
trait ProductProvider {
  def products: Set[String]
  def prices: Map[String, Double]
}

class StandardProductProvider extends ProductProvider {
  override val products: Set[String] = Set("latte")
  override def prices: Map[String, Double] = Map("latte" -> 3.0)
}

class OrderService @Inject()(productProvider: ProductProvider) {
  def create(order: Order): Future[AcknowledgedOrder] = {
    if (validateOrder(order)) {
      val totalPrice = order.items.map(calculateItemPrice).sum
      Future.value(AcknowledgedOrder(order.location, order.items, totalPrice, "GBP", "/order/1234/payment"))
    } else {
      Future.exception(new RuntimeException("invalid order"))
    }
  }

  private def calculateItemPrice(item: OrderItem): Double = {
    productProvider.prices(item.name) * item.quantity
  }

  private def validateOrder(order: Order) = {
    order.items.map(item => productProvider.products.contains(item.name))
      .foldLeft(true)(_ && _)
  }


}

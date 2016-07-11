package com.jensraaby.restbucks.orders

case class Order(location: String, items: Seq[OrderItem])
case class OrderItem(name: String, quantity: Int, milk: String, size: String)
case class AcknowledgedOrder(location: String, items: Seq[OrderItem], cost: BigDecimal, currency: String, linkPayment: String)

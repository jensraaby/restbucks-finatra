package com.jensraaby.restbucks.orders

case class AcknowledgedOrder(location: String, items: Seq[OrderItem], cost: BigDecimal, currency: String, linkPayment: String)

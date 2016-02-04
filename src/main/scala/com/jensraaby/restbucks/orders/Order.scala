package com.jensraaby.restbucks.orders

case class Order(location: String, items: Seq[OrderItem])

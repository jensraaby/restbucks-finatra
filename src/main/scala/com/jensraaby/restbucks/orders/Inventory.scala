package com.jensraaby.restbucks.orders

import com.google.inject.ImplementedBy

@ImplementedBy(classOf[HardcodedInventory])
trait Inventory {
  def products: Set[String]

  def prices: Map[String, Double]
}

class HardcodedInventory extends Inventory {
  override val products: Set[String] = Set("latte")

  override def prices: Map[String, Double] = Map("latte" -> 3.0)
}

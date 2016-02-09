package com.jensraaby.restbucks.controllers

import javax.inject.Inject

import com.jensraaby.restbucks.orders.{Order, OrderService}
import com.twitter.finatra.http.Controller


class OrderController @Inject()(orderService: OrderService) extends Controller {

  post("/order") { order: Order =>
    orderService.create(order)
  }
}

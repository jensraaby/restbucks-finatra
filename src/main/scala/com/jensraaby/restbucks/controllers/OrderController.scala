package com.jensraaby.restbucks.controllers

import javax.inject.Inject

import com.jensraaby.restbucks.orders.{Order, OrderService}
import com.twitter.finagle.http.Request
import com.twitter.finatra.domain.WrappedValue
import com.twitter.finatra.http.Controller


case class WrappedOrder(data: OrderReq) extends WrappedValue[OrderReq]
case class OrderReq(id: String)

class OrderController @Inject()(orderService: OrderService) extends Controller {

  post("/order") { order: Order =>
    orderService.create(order)
  }

//  get("/orders") { request: Request =>
//    request.encodeString()
//  }

  get("/orders/:id") { request: Request =>
    WrappedOrder(OrderReq(request.params("id")))
  }
}

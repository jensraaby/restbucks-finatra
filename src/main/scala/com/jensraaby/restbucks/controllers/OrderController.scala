package com.jensraaby.restbucks.controllers

import javax.inject.Inject

import com.jensraaby.restbucks.orders.{Order, OrderService}
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.inject.domain.WrappedValue


case class WrappedOrder(data: OrderReq) extends WrappedValue[OrderReq]
case class OrderReq(id: String)

class OrderController @Inject()(orderService: OrderService) extends Controller {

  post("/order") { order: Order =>
    time("creating order took %d millis") {
      orderService.create(order)
    }
  }

//  get("/orders") { request: Request =>
//    request.encodeString()
//  }

  get("/orders/:id") { request: Request =>
    infoResult("Looked up order: %s") {
      WrappedOrder(OrderReq(request.params("id")))
    }
  }
}

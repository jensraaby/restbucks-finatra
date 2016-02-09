package com.jensraaby.restbucks

import com.jensraaby.restbucks.controllers.{IndexController, OrderController}
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter


object RestbucksServerMain extends RestbucksServer

class RestbucksServer extends HttpServer {

  override def configureHttp(router: HttpRouter): Unit = {

    router
      .filter[CommonFilters]
      .add[IndexController]
      .add[OrderController]
  }
}

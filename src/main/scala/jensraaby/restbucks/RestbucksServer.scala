package jensraaby.restbucks

import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter
import jensraaby.restbucks.controllers.IndexController


object RestbucksServerMain extends RestbucksServer

class RestbucksServer extends HttpServer {

  override def configureHttp(router: HttpRouter): Unit = {

    router
      .filter[CommonFilters]
      .add(new IndexController)
  }
}

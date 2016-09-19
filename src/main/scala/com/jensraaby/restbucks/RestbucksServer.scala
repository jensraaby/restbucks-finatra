package com.jensraaby.restbucks

import com.google.inject.Module
import com.jensraaby.restbucks.controllers.{OrderController, StaticFilesController}
import com.jensraaby.restbucks.modules.CorsModule
import com.twitter.finagle.http.filter.Cors
import com.twitter.finatra.http.HttpServer
import com.twitter.finatra.http.filters.CommonFilters
import com.twitter.finatra.http.routing.HttpRouter


object RestbucksServerMain extends RestbucksServer

class RestbucksServer extends HttpServer {
  override val name = "restbucks"
  override val disableAdminHttpServer = false

  override protected def modules: Seq[Module] = Seq(CorsModule)

  override def configureHttp(router: HttpRouter): Unit =
    router
      .filter[CommonFilters]
      .filter[Cors.HttpFilter]
      .add[OrderController]
      .add[StaticFilesController]
}

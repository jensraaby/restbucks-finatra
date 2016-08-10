package com.jensraaby.restbucks.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class StaticFilesController extends Controller {

  get("/") { req: Request =>
    response.ok.file("/index.html")
  }

  get("/:*") { request: Request =>
    response.ok.file("/" + request.params("*"))
  }
}

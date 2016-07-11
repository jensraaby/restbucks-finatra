package com.jensraaby.restbucks.controllers

import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller

class StaticFilesController extends Controller {

  get("/") { req: Request =>
    response.ok.file("/index.html")
  }

  get("/js:*") { request: Request =>
    response.ok.file("/js" + request.params("*"))
  }

  get("/css:*") { request: Request =>
    response.ok.file("/css" + request.params("*"))
  }
}

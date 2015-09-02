package jensraaby.restbucks.controllers

import com.twitter.finagle.httpx.Request
import com.twitter.finatra.http.Controller
import com.twitter.util.Future


class IndexController extends Controller {

  // This is a function, not a method!
  val handleHelloRequest = { req: Request =>
    Future.value("Hello World")
  }

  get("/")(handleHelloRequest)


}

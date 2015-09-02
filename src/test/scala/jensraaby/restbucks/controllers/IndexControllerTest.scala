package jensraaby.restbucks.controllers

import com.twitter.finagle.httpx.{RequestBuilder, Method}
import org.scalatest.FunSpec

class IndexControllerTest extends FunSpec {

  describe("Index handler") {
    it("should return hello world for any request") {
      val controller = new IndexController
      val request = RequestBuilder.create().url("http://blah").build(Method.Get, None)

      val response = controller.handleHelloRequest(request)

    }
  }
}

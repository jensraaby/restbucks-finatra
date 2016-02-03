package com.jensraaby.restbucks.controllers

import javax.inject.Inject

import com.jensraaby.restbucks.modules.BBCWebHttpClient
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.httpclient.HttpClient

case class Story(headline: String)

case class BBCHeadlines(topStory: Story)

class BbcController @Inject()(@BBCWebHttpClient bbCWebHttpClient: HttpClient) extends Controller {

  get("/bbc") { request: Request =>
    response.ok(BBCHeadlines(Story("Something happened")))
  }


}

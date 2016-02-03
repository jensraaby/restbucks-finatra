package com.jensraaby.restbucks.controllers

import javax.inject.Inject

import com.jensraaby.restbucks.modules.BBCWebHttpClient
import com.twitter.finagle.http.Request
import com.twitter.finatra.http.Controller
import com.twitter.finatra.httpclient.{RequestBuilder, HttpClient}

case class Story(headline: String)

case class BBCHeadlines(topStory: Story)

class BbcController @Inject()(@BBCWebHttpClient bbcWebHttpClient: HttpClient) extends Controller {

  get("/bbc") { request: Request =>
    val newsPage = bbcWebHttpClient.execute(RequestBuilder.get("/news"))
    newsPage.map { resp =>
      val html = resp.contentString

      response.ok(BBCHeadlines(Story("Something happened: " + html)))
    }
  }


}

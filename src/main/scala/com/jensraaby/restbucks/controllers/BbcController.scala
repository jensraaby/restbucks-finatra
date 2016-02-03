package com.jensraaby.restbucks.controllers

import javax.inject.Inject

import com.jensraaby.restbucks.modules.BBCWebHttpClient
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finatra.http.Controller
import com.twitter.finatra.httpclient.{RequestBuilder, HttpClient}
import org.jsoup.Jsoup

case class Story(headline: String, link: String)

case class BBCHeadlines(topStory: Story)

class BbcController @Inject()(@BBCWebHttpClient bbcWebHttpClient: HttpClient) extends Controller {

  get("/bbc") { request: Request =>
     bbcWebHttpClient.execute(RequestBuilder.get("/news")) map { resp =>
      val page = new BBCNewsFrontPage(resp)
      val headlines = BBCHeadlines(Story(page.topStoryHeadline, page.topStoryLink))

      response.ok(headlines)
    }
  }
}

class BBCNewsFrontPage(response: Response) {
  val document = Jsoup.parse(response.contentString)

  lazy val topStoryHeadline = document.select("#comp-top-story-1 h3 span").text()
  lazy val topStoryLink = document.select("#comp-top-story-1 a[href").attr("href")
  lazy val links = document.select("a[href]").attr("href")
}

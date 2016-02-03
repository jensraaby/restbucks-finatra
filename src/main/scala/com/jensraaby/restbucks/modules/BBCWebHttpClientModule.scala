package com.jensraaby.restbucks.modules

import com.google.inject.{Provides, Singleton}
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Response, Request}
import com.twitter.finatra.httpclient.{RichHttpClient, HttpClient}
import com.twitter.finatra.json.FinatraObjectMapper
import com.twitter.inject.TwitterModule

object BBCWebHttpClientModule extends TwitterModule {
  val hostname = "www.bbc.co.uk"

  @Singleton
  @Provides
  @BBCWebHttpClient
  def provideHttpClient(defaultMapper: FinatraObjectMapper,
                        @BBCWebHttpClient httpService: Service[Request, Response]): HttpClient = {

    new HttpClient(
      hostname = hostname,
      httpService = httpService,
      retryPolicy = None,
      defaultHeaders = Map(),
      mapper = defaultMapper
    )
  }

  @Singleton
  @Provides
  @BBCWebHttpClient
  def provideHttpService: Service[Request,Response] = {
    RichHttpClient.newClientService(
      dest = hostname + ":80"
    )
  }
}


package com.jensraaby.restbucks.modules

import com.google.inject.Provides
import com.twitter.finagle.http.filter.Cors
import com.twitter.inject.TwitterModule

object CorsModule extends TwitterModule {

  @Provides
  def corsPolicy: Cors.Policy =
    // see http://www.html5rocks.com/en/tutorials/cors/
    Cors.Policy(
      origin => Some(origin),
      method => Some(method :: Nil),
      headers => Some(headers),
      supportsCredentials = true
    )


  @Provides
  def corsFilter(policy: Cors.Policy): Cors.HttpFilter = new Cors.HttpFilter(policy)
}

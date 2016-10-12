package com.jensraaby.restbucks.modules

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.codahale.metrics.{Meter, MetricFilter}
import com.twitter.inject.{Injector, TwitterModule}
import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.google.inject.{Provides, Singleton}
import com.twitter.finagle.metrics.MetricsStatsReceiver

object MetricReportingModule extends TwitterModule {

  private lazy val graphiteHost = new InetSocketAddress("localhost", 2003)
  private lazy val graphite = new Graphite(graphiteHost)

  private lazy val reporter = GraphiteReporter.forRegistry(MetricsStatsReceiver.metrics)
    .prefixedWith("restbucks")
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .filter(MetricFilter.ALL)
    .build(graphite)

  override def singletonStartup(injector: Injector): Unit = {
    super.singletonStartup(injector)
    info("Starting metrics reporter on 1m interval")
    reporter.start(10, TimeUnit.SECONDS)
  }
}

package com.jensraaby.restbucks.modules

import java.net.InetSocketAddress
import java.util.concurrent.TimeUnit

import com.codahale.metrics.{Metric, MetricFilter}
import com.codahale.metrics.graphite.{Graphite, GraphiteReporter}
import com.twitter.finagle.metrics.MetricsStatsReceiver
import com.twitter.inject.{Injector, TwitterModule}

object MetricReportingModule extends TwitterModule {

  private lazy val graphiteHost = new InetSocketAddress("localhost", 2003)
  private lazy val graphite = new Graphite(graphiteHost)

  private lazy val reporter = GraphiteReporter.forRegistry(MetricsStatsReceiver.metrics)
    .prefixedWith("restbucks")
    .convertRatesTo(TimeUnit.SECONDS)
    .convertDurationsTo(TimeUnit.MILLISECONDS)
    .filter(interestingMetricsFilter)
    .build(graphite)

  private lazy val interestingMetrics = List(
    "scheduler.blocking_ms",
    "scheduler.dispatches.00",

    "service.failure.mean_rate",
    "route.GET.requests.count",
    "time.2XX.p99",
    "time.2XX.p95",
    "time.2XX.stddev",
    "time.2XX.mean",

    "response_size.mean",
    "response_size.stddev",

    "jvm.application_time_millis",
    "jvm.classes.current_loaded",
    "jvm.classes.total_loaded",
    "jvm.classes.total_unloaded",
    "jvm.compilation.time_msec",
    "jvm.fd_count",
    "jvm.fd_limit",
    "jvm.gc.PS_MarkSweep.cycles ",
    "jvm.gc.PS_MarkSweep.msec",
    "jvm.gc.PS_Scavenge.cycles ",
    "jvm.gc.PS_Scavenge.msec",
    "jvm.gc.cycles",
    "jvm.gc.msec",
    "jvm.heap.committed",
    "jvm.heap.max",
    "jvm.heap.used",
    "jvm.mem.allocations.eden.bytes",
    "jvm.mem.buffer.direct.count",
    "jvm.mem.buffer.direct.max",
    "jvm.mem.buffer.direct.used",
    "jvm.mem.buffer.mapped.count",
    "jvm.mem.buffer.mapped.max",
    "jvm.mem.buffer.mapped.used",
    "jvm.mem.current.Code_Cache.max",
    "jvm.mem.current.Code_Cache.used",
    "jvm.mem.current.Compressed_Class_Space.max",
    "jvm.mem.current.Compressed_Class_Space.used",
    "jvm.mem.current.Metaspace.max -",
    "jvm.mem.current.Metaspace.used",
    "jvm.mem.current.PS_Eden_Space.max",
    "jvm.mem.current.PS_Eden_Space.used",
    "jvm.mem.current.PS_Old_Gen.max",
    "jvm.mem.current.PS_Old_Gen.used",
    "jvm.mem.current.PS_Survivor_Space.max",
    "jvm.mem.current.PS_Survivor_Space.used ",
    "jvm.mem.current.used",
    "jvm.mem.metaspace.max_capacity",
    "jvm.mem.postGC.PS_Eden_Space.max",
    "jvm.mem.postGC.PS_Eden_Space.used ",
    "jvm.mem.postGC.PS_Old_Gen.max",
    "jvm.mem.postGC.PS_Old_Gen.used",
    "jvm.mem.postGC.PS_Survivor_Space.max",
    "jvm.mem.postGC.PS_Survivor_Space.used ",
    "jvm.mem.postGC.used",
    "jvm.nonheap.committed",
    "jvm.nonheap.max -",
    "jvm.nonheap.used",
    "jvm.num_cpus ",
    "jvm.postGC.PS_Eden_Space.max",
    "jvm.postGC.PS_Eden_Space.used",
    "jvm.postGC.PS_Old_Gen.max",
    "jvm.postGC.PS_Old_Gen.used",
    "jvm.postGC.PS_Survivor_Space.max",
    "jvm.postGC.PS_Survivor_Space.used",
    "jvm.postGC.used",
    "jvm.safepoint.count",
    "jvm.safepoint.sync_time_millis",
    "jvm.safepoint.total_time_millis",
    "jvm.start_time",
    "jvm.tenuring_threshold",
    "jvm.thread.count",
    "jvm.thread.daemon_count",
    "jvm.thread.peak_count",
    "jvm.uptime",
    "jvm.gc.eden.pause_msec.count",
    "jvm.gc.eden.pause_msec.max ",
    "jvm.gc.eden.pause_msec.mean",
    "jvm.gc.eden.pause_msec.min ",
    "jvm.gc.eden.pause_msec.stddev",
    "jvm.gc.eden.pause_msec.p50",
    "jvm.gc.eden.pause_msec.p75",
    "jvm.gc.eden.pause_msec.p95",
    "jvm.gc.eden.pause_msec.p98",
    "jvm.gc.eden.pause_msec.p99",
    "jvm.gc.eden.pause_msec.p999"
  )

  private lazy val interestingMetricsFilter = new MetricFilter {
    override def matches(name: String, metric: Metric): Boolean = {
      interestingMetrics.contains(name)
    }
  }

  override def singletonStartup(injector: Injector): Unit = {
    super.singletonStartup(injector)
    info("Starting metrics reporter on 10s interval")
    reporter.start(10, TimeUnit.SECONDS)
  }
}

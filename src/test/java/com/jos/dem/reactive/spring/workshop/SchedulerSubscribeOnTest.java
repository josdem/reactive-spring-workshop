package com.jos.dem.reactive.spring.workshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchedulerSubscribeOnTest {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Test
  @DisplayName("validate fast subscribers")
  void shouldSubscribeOn() {
    var rsbThreadName = SchedulerSubscribeOnTest.class.getName();
    var map = new ConcurrentHashMap<String, AtomicInteger>();
    var executor =
        Executors.newFixedThreadPool(
            5,
            runnable -> {
              Runnable wrapper =
                  () -> {
                    var key = Thread.currentThread().getName();
                    var result = map.computeIfAbsent(key, s -> new AtomicInteger());
                    result.incrementAndGet();
                    runnable.run();
                  };
              return new Thread(wrapper, rsbThreadName);
            });
    Scheduler scheduler = Schedulers.fromExecutor(executor);
    Mono<Integer> integerFlux =
        Mono.just(1)
            .subscribeOn(scheduler)
            .doFinally(signal -> map.forEach((k, v) -> log.info("k: {}, v: {}", k, v)));
    StepVerifier.create(integerFlux).expectNextCount(1).verifyComplete();
    var atomicInteger = map.get(rsbThreadName);
    assertEquals(1, atomicInteger.get(), "should be one");
  }
}

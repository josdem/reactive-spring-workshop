package com.jos.dem.reactive.spring.workshop.intermediate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SchedulersHookTest {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Test
  @DisplayName("should validate thread in executor")
  void shouldValidateOnSchedulerHook() {
    var counter = new AtomicInteger();
    Schedulers.onScheduleHook(
        "hook",
        runnable ->
            () -> {
              var threadName = Thread.currentThread().getName();
              counter.incrementAndGet();
              log.info("Before thread execution {}", threadName);
              runnable.run();
              log.info("After thread execution {}", threadName);
            });
    Flux<Integer> integerFlux =
        Flux.just(1, 2, 3).delayElements(Duration.ofMillis(1)).subscribeOn(Schedulers.immediate());
    StepVerifier.create(integerFlux).expectNext(1, 2, 3).verifyComplete();
    assertEquals(3, counter.get(), "count should be three");
  }
}

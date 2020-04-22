package com.jos.dem.reactive.spring.workshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Signal;
import reactor.core.publisher.SignalType;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DoOnTest {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Test
  @DisplayName("do on test examples")
  void shouldDoOn() {
    var signals = new ArrayList<Signal<Integer>>();
    var nextValues = new ArrayList<Integer>();
    var subscriptions = new ArrayList<Subscription>();
    var exceptions = new ArrayList<Throwable>();
    var finallySignals = new ArrayList<SignalType>();

    Flux<Integer> on =
        Flux.<Integer>create(
                sink -> {
                  sink.next(1);
                  sink.next(2);
                  sink.next(3);
                  sink.error(new RuntimeException("Error"));
                  sink.complete();
                })
            .doOnNext(nextValues::add)
            .doOnEach(signals::add)
            .doOnSubscribe(subscriptions::add)
            .doOnError(RuntimeException.class, exceptions::add)
            .doFinally(finallySignals::add);

    StepVerifier.create(on).expectNext(1, 2, 3).expectError(RuntimeException.class).verify();

    signals.forEach(signal -> log.info("signal: {}", signal));
    assertEquals(4, signals.size(), "should be 4 signals");

    finallySignals.forEach(signal -> log.info("finally signal: {}", signal));
    assertEquals(1, finallySignals.size(), "should be one final signal");

    subscriptions.forEach(subscription -> log.info("subscription: {}", subscription));
    assertEquals(1, subscriptions.size(), "should be one subscription");

    exceptions.forEach(exception -> log.info("exception: {}", exception.getMessage()));
    assertEquals(1, exceptions.size(), "should be one exception");
    assertTrue(exceptions.get(0) instanceof RuntimeException);

    nextValues.forEach(value -> log.info("value: {}", value));
    assertEquals(Arrays.asList(1, 2, 3), nextValues, "should have 1,2,3 as next values");
  }
}

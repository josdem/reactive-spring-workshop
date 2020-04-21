package com.jos.dem.reactive.spring.workshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class TakeTest {

  @Test
  @DisplayName("take test examples")
  void shouldTakeUntilCount() {
    var count = 10;
    Flux<Integer> take = getRange().take(count);
    StepVerifier.create(take).expectNextCount(10).verifyComplete();
  }

  @Test
  @DisplayName("take until test example")
  void shouldTakeUntil() {
    var count = 50;
    Flux<Integer> take = getRange().takeUntil(i -> i == (count - 1));
    StepVerifier.create(take).expectNextCount(count).verifyComplete();
  }

  private Flux<Integer> getRange() {
    return Flux.range(0, 1000);
  }
}

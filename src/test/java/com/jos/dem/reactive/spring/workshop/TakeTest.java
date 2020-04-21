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
    Flux<Integer> take = Flux.range(0, 1000).take(count);
    StepVerifier.create(take).expectNextCount(10).verifyComplete();
  }
}

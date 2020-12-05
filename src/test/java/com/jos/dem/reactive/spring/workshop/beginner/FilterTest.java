package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class FilterTest {

  @Test
  @DisplayName("filter example")
  void shouldFilter() {
    Flux<Integer> range = Flux.range(0, 1000).take(5);
    Flux<Integer> filter = range.filter(i -> i % 2 == 0);
    StepVerifier.create(filter).expectNext(0, 2, 4).verifyComplete();
  }
}

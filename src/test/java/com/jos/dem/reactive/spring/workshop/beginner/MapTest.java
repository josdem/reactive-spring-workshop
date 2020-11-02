package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class MapTest {

  @Test
  @DisplayName("testing Map on Flux")
  void shouldTestMap() {
    var data = Flux.just("a", "b", "c").map(String::toUpperCase);
    StepVerifier.create(data).expectNext("A", "B", "C").verifyComplete();
  }
}

package com.jos.dem.reactive.spring.workshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

public class SwitchMapTest {

  @Test
  @DisplayName("switch map test")
  void shouldSwitchMap() {
    Flux<String> source =
        Flux.just("re", "rea", "reac", "react", "reactive")
            .delayElements(Duration.ofMillis(100))
            .switchMap(this::lookup);
    StepVerifier.create(source).expectNext("reactive -> reactive").verifyComplete();
  }

  private Flux<String> lookup(String keyword) {
    return Flux.just(keyword + " -> reactive").delayElements(Duration.ofMillis(500));
  }
}

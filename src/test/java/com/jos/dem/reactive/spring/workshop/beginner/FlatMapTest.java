package com.jos.dem.reactive.spring.workshop.beginner;

import com.jos.dem.reactive.spring.workshop.model.Pair;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

class FlatMapTest {

  @Test
  @DisplayName("flat map test")
  void shouldFlatMap() {
    Flux<Integer> data =
        Flux.just(new Pair(1, 300), new Pair(2, 200), new Pair(3, 100))
            .flatMap(pair -> this.delayReplyFor(pair.getId(), pair.getDelay()));
    StepVerifier.create(data).expectNext(3, 2, 1).verifyComplete();
  }

  private Flux<Integer> delayReplyFor(Integer i, long delay) {
    return Flux.just(i).delayElements(Duration.ofMillis(delay));
  }
}

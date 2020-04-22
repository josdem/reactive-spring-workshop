package com.jos.dem.reactive.spring.workshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HandleTest {

  @Test
  @DisplayName("handle test example")
  void shouldHandleEvents() {
    StepVerifier.create(this.handle(5, 4))
        .expectNext(0, 1, 2, 3)
        .expectError(RuntimeException.class)
        .verify();
  }

  private Flux<Integer> handle(int max, int numError) {
    return Flux.range(0, max)
        .handle(
            (value, sink) -> {
              var upTo =
                  Stream.iterate(0, i -> i < numError, i -> i + 1).collect(Collectors.toList());
              if (upTo.contains(value)) {
                sink.next(value);
                return;
              }
              if (value == numError) {
                sink.error(new RuntimeException("No 4 to deliver!"));
                return;
              }
              sink.complete();
            });
  }
}

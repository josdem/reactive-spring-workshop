package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class TransformTest {

  @Test
  @DisplayName("Transform operator")
  void shouldTransform() {
    var finished = new AtomicBoolean();
    var letters =
        Flux.just("A", "B", "C")
            .transform(stringFlux -> stringFlux.doFinally(signal -> finished.set(true)));
    StepVerifier.create(letters).expectNextCount(3).verifyComplete();

    assertTrue(finished.get(), "Finished boolean should be true");
  }
}

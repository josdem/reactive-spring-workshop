package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThenManyTest {

  @Test
  @DisplayName("Then Many")
  void thenMany() {
    var letters = new AtomicInteger();
    var numbers = new AtomicInteger();

    Flux<String> lettersPublisher =
        Flux.just("A", "B", "C").doOnNext(value -> letters.incrementAndGet());

    Flux<Integer> numbersPublisher =
            Flux.just(1, 2, 3).doOnNext(value -> numbers.incrementAndGet());

    Flux<Integer> thisBeforeThat = lettersPublisher.thenMany(numbersPublisher);
    StepVerifier.create(thisBeforeThat).expectNext(1,2,3).verifyComplete();

    assertEquals(3, letters.get(), "should be three");
    assertEquals(3, numbers.get(), "should be three");
  }
}

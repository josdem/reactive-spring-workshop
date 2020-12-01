package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

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

    @Test
    @DisplayName("take duration test example")
    void shouldTakeDuration() {
        var seconds = 5;
        Flux<Integer> take = getInterval().take(Duration.ofSeconds(seconds));
        StepVerifier.create(take).expectNextCount(5).verifyComplete();
    }

    private Flux<Integer> getRange() {
        return Flux.range(0, 1000);
    }

    private Flux<Integer> getInterval() {
        return Flux.interval(Duration.ofSeconds(1)).map(it -> it.intValue());
    }
}

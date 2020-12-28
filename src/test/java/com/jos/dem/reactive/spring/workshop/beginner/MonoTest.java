package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

class MonoTest {

    @Test
    @DisplayName("Mono test")
    void shouldDelayElements(){
        Mono<String> result = Mono.just("Hello Bummer!")
                .delayElement(Duration.ofSeconds(1));
        StepVerifier.create(result).expectNext("Hello Bummer!").verifyComplete();
    }
}

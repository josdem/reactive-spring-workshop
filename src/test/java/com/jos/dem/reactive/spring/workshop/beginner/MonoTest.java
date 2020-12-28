package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

class MonoTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    @DisplayName("Mono test")
    void shouldDelayElements(){
        Mono<String> result = Mono.just("Hello Bummer!")
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(it -> log.info("hello world!"))
                .delayElement(Duration.ofSeconds(1))
                .doOnNext(it -> log.info("hello josdem!"))
                .doOnTerminate(() -> log.info("Complete!"));
        StepVerifier.create(result).expectNext("Hello Bummer!").verifyComplete();
    }
}

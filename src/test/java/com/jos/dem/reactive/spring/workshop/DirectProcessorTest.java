package com.jos.dem.reactive.spring.workshop;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.DirectProcessor;
import reactor.test.StepVerifier;

/**
 * DirectProcessor can have multiple consumers
 */
class DirectProcessorTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    @DisplayName("create a direct processor")
    void shouldCreateADirectProcessor(){
        DirectProcessor<Integer> data = DirectProcessor.create();
        data.subscribe(it -> log.info("it: {}", it), e -> log.info("error"), () -> log.info("Finished 1"));
        data.subscribe(it -> log.info("it: {}", it), e -> log.info("error"), () -> log.info("Finished 2"));
        data.onNext(1);
        data.onNext(2);
        data.onNext(3);
        data.onComplete();

        StepVerifier.create(data).expectSubscription();
        StepVerifier.create(data).verifyComplete();
    }
}

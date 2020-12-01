package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.FluxSink;
import reactor.test.StepVerifier;


class EmitterProcessorTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Test
    @DisplayName("should create an emitter processor")
    void shouldGenerateEmitterProcessor() {
        EmitterProcessor<Integer> processor = EmitterProcessor.create();
        produce(processor.sink());
        StepVerifier.create(processor).expectNext(1).expectNext(2).expectNext(3).verifyComplete();
    }

    @Test
    @DisplayName("should consume processor values")
    void shouldConsumeProcessorValues() {
        EmitterProcessor<Integer> processor = EmitterProcessor.create();
        produce(processor.sink());
        processor.subscribe(value -> log.info("value: " + value));
    }

    private void produce(FluxSink<Integer> sink) {
        sink.next(1);
        sink.next(2);
        sink.next(3);
        sink.complete();
    }

}

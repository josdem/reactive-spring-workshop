package com.jos.dem.reactive.spring.workshop.beginner;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.ReplayProcessor;
import reactor.test.StepVerifier;

import java.util.stream.IntStream;

class ReplayProcessorTest {

  @Test
  @DisplayName("should replay processor")
  void shouldReplayProcessor() {
    ReplayProcessor<String> processor = ReplayProcessor.create(2, false);
    produce(processor.sink());
    consume(processor);
  }

  private void produce(FluxSink<String> sink) {
    sink.next("1");
    sink.next("2");
    sink.next("3");
    sink.complete();
  }

  private void consume(Flux<String> publisher) {
    IntStream.range(0, 5)
        .forEach(
            it -> StepVerifier.create(publisher).expectNext("2").expectNext("3").verifyComplete());
  }
}

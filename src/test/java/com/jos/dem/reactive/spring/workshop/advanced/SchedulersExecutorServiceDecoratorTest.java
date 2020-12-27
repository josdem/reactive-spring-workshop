package com.jos.dem.reactive.spring.workshop.advanced;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchedulersExecutorServiceDecoratorTest {

  private static final String RSB = "rsb";

  private final AtomicInteger methodInvocationCounts = new AtomicInteger();

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @BeforeEach
  void before() {
    Schedulers.resetFactory();
    Schedulers.addExecutorServiceDecorator(
        RSB, (scheduler, scheduledExecutorService) -> this.decorate(scheduledExecutorService));
  }

  @Test
  @DisplayName("Change default decorator")
  void shouldChangeDefaultDecorator() {
    Flux<Integer> integerFlux = Flux.just(1).delayElements(Duration.ofMillis(1));
    StepVerifier.create(integerFlux)
        .thenAwait(Duration.ofMillis(10))
        .expectNextCount(1)
        .verifyComplete();

    assertEquals(1, methodInvocationCounts.get(), "should have one invocation");
  }

  private ScheduledExecutorService decorate(ScheduledExecutorService executorService) {
    var proxyFactoryBean = new ProxyFactoryBean();
    try {
      proxyFactoryBean.setProxyInterfaces(new Class[] {ScheduledExecutorService.class});
      proxyFactoryBean.addAdvice(
          (MethodInterceptor)
              methodInvocation -> {
                var methodName = methodInvocation.getMethod().getName().toLowerCase();
                this.methodInvocationCounts.incrementAndGet();
                log.info("methodName: {} incrementing", methodName);
                return methodInvocation.proceed();
              });
      proxyFactoryBean.setSingleton(true);
      proxyFactoryBean.setTarget(executorService);
      return (ScheduledExecutorService) proxyFactoryBean.getObject();
    } catch (ClassNotFoundException cnfe) {
      log.error(cnfe.getMessage());
    }
    return null;
  }

  @AfterEach
  void after() {
    Schedulers.resetFactory();
    Schedulers.removeExecutorServiceDecorator(RSB);
  }
}

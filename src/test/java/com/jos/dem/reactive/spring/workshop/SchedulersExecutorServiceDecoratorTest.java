package com.jos.dem.reactive.spring.workshop;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactoryBean;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

class SchedulersExecutorServiceDecoratorTest {

  private final AtomicInteger methodInvocationCounts = new AtomicInteger();
  private String rsb = "rsb";

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @BeforeEach
  void before() {
    Schedulers.resetFactory();
    Schedulers.addExecutorServiceDecorator(
        this.rsb, (scheduler, scheduledExecutorService) -> this.decorate(scheduledExecutorService));
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
}

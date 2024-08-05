package com.jameswu.demo.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApiTimingAspect {

	@Autowired
	private MeterRegistry meterRegistry;

	@Around("execution(* com.jameswu.demo.controller..*(..))")
	public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
		Timer.Sample sample = Timer.start(meterRegistry);
		Object proceed = joinPoint.proceed();
		sample.stop(meterRegistry.timer(
				"api.execution.time", "method", joinPoint.getSignature().toShortString()));
		return proceed;
	}
}

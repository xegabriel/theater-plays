package com.aop.tpma.aspect.monitoring;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Slf4j
@Aspect
@Component
public class PerformanceMonitorAspect {
    @Around(value = "execution(* com.aop.tpma.service.*.*(..))")
    public Object aroundTrackTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = proceedingJoinPoint.proceed();
        stopWatch.stop();

        log.info("Execution time of {}.{} :: {} ms", methodSignature.getDeclaringType().getSimpleName(), methodSignature.getName(), stopWatch.getTotalTimeMillis());

        return result;
    }

    @Before(value = "@annotation(com.aop.tpma.aspect.monitoring.annotation.MonitorMemoryUsageBefore)")
    public void aroundTrackMemoryUsage(JoinPoint joinPoint) {
        long memoryUsageBytes = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        log.info("Memory usage: {} bytes before executing {}", memoryUsageBytes, joinPoint.getSignature());

    }
}

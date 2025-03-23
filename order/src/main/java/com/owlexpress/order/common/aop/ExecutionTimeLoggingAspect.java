package com.owlexpress.order.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
@Slf4j
public class ExecutionTimeLoggingAspect {

    @Around("execution(* com.owlexpress.order.application.service.OrderServiceImpl.createOrder(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;

        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long end = System.currentTimeMillis();
            long duration = end - start;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "order-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", duration);
            logMap.put("timestamp", Instant.now().toString());

            log.info(new ObjectMapper().writeValueAsString(logMap));
        }
    }
}
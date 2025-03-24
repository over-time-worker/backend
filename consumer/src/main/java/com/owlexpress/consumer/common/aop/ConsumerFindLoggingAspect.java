package com.owlexpress.consumer.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class ConsumerFindLoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.owlexpress.consumer.domain.service.*.find(..)) && args(consumerId)")
    public Object logConsumerFind(ProceedingJoinPoint joinPoint, UUID consumerId) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Exception error = null;

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            long end = System.currentTimeMillis();

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "consumer-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", end - start);
            logMap.put("timestamp", Instant.now().toString());
            logMap.put("consumerId", consumerId.toString());

            if (error == null) {
                logMap.put("status", "SUCCESS");
            } else {
                logMap.put("status", "FAILURE");
                logMap.put("exception", error.getClass().getSimpleName());
                logMap.put("message", error.getMessage());
            }

            log.info(objectMapper.writeValueAsString(logMap));
        }
    }
}
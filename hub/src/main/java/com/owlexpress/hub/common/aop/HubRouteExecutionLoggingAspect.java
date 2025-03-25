package com.owlexpress.hub.common.aop;

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
public class HubRouteExecutionLoggingAspect {

    @Around("execution(* com.owlexpress.hub.application.HubDistanceServiceImpl.findShortestPath(..))")
    public Object logFindShortestPath(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Exception error = null;

        try {
            result = joinPoint.proceed(); // 실제 로직 수행
            return result;
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "hub-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", duration);
            logMap.put("timestamp", Instant.now().toString());

            if (error == null) {
                logMap.put("status", "SUCCESS");
            } else {
                logMap.put("status", "FAILURE");
                logMap.put("exception", error.getClass().getSimpleName());
                logMap.put("message", error.getMessage());
            }

            log.info(new ObjectMapper().writeValueAsString(logMap));
        }
    }
}
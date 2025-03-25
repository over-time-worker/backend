package com.owlexpress.hub.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto.Product;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@Slf4j
public class HubProductOrderLoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Around("execution(* com.owlexpress.hub.application.HubProductUseCaseImpl.confirmOrder(..))")
    public Object logConfirmOrderExecution(ProceedingJoinPoint joinPoint) throws Throwable {
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
            long duration = System.currentTimeMillis() - start;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "hub-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", duration);
            logMap.put("timestamp", Instant.now().toString());

            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0 && args[0] instanceof OrderConfirmRequestDto request) {
                logMap.put("latitude", request.getLatitude());
                logMap.put("longitude", request.getLongitude());
                logMap.put("orderedProductIds", request.getOrderProducts().stream()
                        .map(Product::getProductId)
                        .map(UUID::toString)
                        .toList());
            }

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
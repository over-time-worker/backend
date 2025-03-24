package com.owlexpress.deliverymanager.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.deliverymanager.presentation.dto.request.DeliveryManagerRequestDto;
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
public class DeliveryManagerAssignLoggingAspect {

    @Around("execution(* com.owlexpress.deliverymanager.application.usecase.ConsumerDeliveryManagerUsecase.assign(..))")
    public Object logAssignExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;
        Exception error = null;

        try {
            result = joinPoint.proceed(); // 실제 메서드 실행
            return result;
        } catch (Exception e) {
            error = e;
            throw e;
        } finally {
            long duration = System.currentTimeMillis() - start;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "deliverymanager-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", duration);
            logMap.put("timestamp", Instant.now().toString());

            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Object dto = args[0];
                if (dto instanceof DeliveryManagerRequestDto requestDto) {
                    logMap.put("currentHubId", requestDto.getCurrentHubId());
                    logMap.put("deliveryId", requestDto.getDeliveryId());
                }
            }

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
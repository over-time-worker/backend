package com.owlexpress.delivery.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.delivery.application.dtos.request.DeliveryManagerRequestDto;
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
public class DeliveryManagerExecutionLoggingAspect {

    @Around("execution(* com.owlexpress.delivery.application.service.DeliveryUsecase..assign(..))")
    public Object logAssignExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed(); // 실제 메서드 실행
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "delivery-manager-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", duration);
            logMap.put("timestamp", Instant.now().toString());

            // 인자 중에서 request DTO의 허브 ID 추출
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Object dto = args[0];
                if (dto instanceof DeliveryManagerRequestDto requestDto) {
                    logMap.put("currentHubId", requestDto.getCurrentHubId());
                    logMap.put("nextHubId", requestDto.getNextHubId());
                    logMap.put("deliveryId", requestDto.getDeliveryId());
                    logMap.put("orderId", requestDto.getOrderId());
                }
            }

            log.info(new ObjectMapper().writeValueAsString(logMap));
        }
    }
}
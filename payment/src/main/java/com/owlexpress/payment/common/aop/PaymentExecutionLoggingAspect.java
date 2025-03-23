package com.owlexpress.payment.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
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
public class PaymentExecutionLoggingAspect {

    @Around("execution(* com.owlexpress.payment.application.PaymentUseCase.createPayment(..))")
    public Object logCreatePaymentExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;

            // Logging details
            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "payment-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", duration);
            logMap.put("timestamp", Instant.now().toString());

            // Extract arguments (optional)
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Object dto = args[0];
                if (dto instanceof PaymentCreateRequestDto requestDto) {
                    logMap.put("orderId", requestDto.getOrderId());
                    logMap.put("consumerCompanyId", requestDto.getConsumerCompanyId());
                    logMap.put("startHubId", requestDto.getStartHubId());
                }
            }

            log.info(new ObjectMapper().writeValueAsString(logMap));
        }
    }
}
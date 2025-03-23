package com.owl_express.alarm.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
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
public class AlarmExecutionLoggingAspect {

    @Around("execution(* com.owl_express.alarm.application.service.AlarmUsecase..createAlarmForCompanyDeliver(..))")
    public Object logCreateAlarmExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = null;

        try {
            result = joinPoint.proceed(); // 메서드 실행
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;

            Map<String, Object> logMap = new HashMap<>();
            logMap.put("type", "execution-time");
            logMap.put("service", "alarm-service");
            logMap.put("method", joinPoint.getSignature().toShortString());
            logMap.put("duration_ms", duration);
            logMap.put("timestamp", Instant.now().toString());

            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                Object dto = args[0];
                if (dto instanceof AlarmCreateRequestDto requestDto) {
                    logMap.put("deliverChannelId", requestDto.getDeliverChannelId());
                    logMap.put("platform", requestDto.getPlatformName());
                }
            }

            log.info(new ObjectMapper().writeValueAsString(logMap));
        }
    }
}
package com.owl_express.alarm.application.exceptions;

import com.owl_express.alarm.common.dto.CommonDto;
import feign.FeignException;
import org.springframework.http.ResponseEntity;

public interface FeignExceptionHandlerStrategy {
    boolean supports(FeignException e); //특정 예외를 처리할 수 있는지 확인
    ResponseEntity<CommonDto<Void>> handleException(FeignException exception);
}

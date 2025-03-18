package com.owlexpress.consumer.infrastructure.feignClient;

import com.owlexpress.consumer.common.CommonDto;
import com.owlexpress.consumer.common.dto.response.GetUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/{userId}")
    CommonDto<GetUserInfoResponseDto> get(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable("userId") Long userId
    );
}

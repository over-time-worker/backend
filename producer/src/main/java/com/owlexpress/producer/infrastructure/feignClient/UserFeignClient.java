package com.owlexpress.producer.infrastructure.feignClient;

import com.owlexpress.producer.common.CommonDto;
import com.owlexpress.producer.common.dto.response.GetUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    //User정보 조회
    @GetMapping("/api/users/{userId}")
    CommonDto<GetUserInfoResponseDto> get(
            @PathVariable("userId") Long userId
    );
}

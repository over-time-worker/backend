package com.owlexpress.consumer.infrastructure.feignClient;

import com.owlexpress.consumer.common.dto.CommonDto;
import com.owlexpress.consumer.common.dto.response.GetUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    @GetMapping("/api/users")
    CommonDto<GetUserInfoResponseDto> get(
            @RequestHeader("X-User-Passport") String passport
    );
}

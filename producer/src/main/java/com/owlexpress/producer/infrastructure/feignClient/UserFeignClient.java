package com.owlexpress.producer.infrastructure.feignClient;

import com.owlexpress.producer.common.CommonDto;
import com.owlexpress.producer.common.dto.response.GetUserInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    //User정보 조회
    @GetMapping("/api/users")
    CommonDto<GetUserInfoResponseDto> get(
            @RequestHeader("X-User-Passport") String passport
    );
}

package com.owlexpress.auth.infrastructure;

import com.owlexpress.auth.application.dto.request.UserLoginRequestDto;
import com.owlexpress.auth.application.dto.response.UserInfoResponseDto;
import com.owlexpress.auth.presentation.dto.request.LoginRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "user-service")
public interface UserClient {

    /* TODO: 애플리케이션 계층과 의존성 분리
    ex: infrastructure 계층에 DTO 클래스 추가 후 해당 DTO 클래스를 application 계층에서 사용하는 DTO 클래스로 변환
    */
    @PostMapping("/api/users/signin")
    public UserInfoResponseDto sigIn(UserLoginRequestDto requestDto);
}

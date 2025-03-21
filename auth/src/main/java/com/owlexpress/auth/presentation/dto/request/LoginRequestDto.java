package com.owlexpress.auth.presentation.dto.request;

import com.owlexpress.auth.application.dto.request.UserLoginRequestDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {

    private String username;
    private String password;

    @Builder
    public LoginRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // TODO: application 계층과 의존성 분리하기
    public UserLoginRequestDto toUserLoginRequestDto() {
        return UserLoginRequestDto.builder()
                .accountId(this.username)
                .password(this.password)
                .build();
    }
}

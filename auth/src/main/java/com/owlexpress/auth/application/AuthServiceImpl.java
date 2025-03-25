package com.owlexpress.auth.application;

import com.owlexpress.auth.application.dto.request.UserLoginRequestDto;
import com.owlexpress.auth.application.dto.response.UserInfoResponseDto;
import com.owlexpress.auth.common.TokenProvider;
import com.owlexpress.auth.infrastructure.UserClient;
import com.owlexpress.auth.presentation.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserClient userClient;
    private final TokenProvider provider;

    @Override
    public String signIn(UserLoginRequestDto userLoginRequestDto) {
        UserInfoResponseDto responseDto = userClient.sigIn(userLoginRequestDto).getData();
        return provider.generateAccessToken(responseDto.getUserId(), responseDto.getRole());
    }
}

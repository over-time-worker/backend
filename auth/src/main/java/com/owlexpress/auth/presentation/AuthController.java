package com.owlexpress.auth.presentation;

import com.owlexpress.auth.application.AuthService;
import com.owlexpress.auth.presentation.dto.request.LoginRequestDto;
import com.owlexpress.auth.presentation.dto.response.TokenResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // TODO : 리프레시 토큰도 같이 발급.

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponseDto> signIn(LoginRequestDto requestDto, HttpServletResponse response) {
        response.setHeader("Authorization", authService.signIn(requestDto.toUserLoginRequestDto()));

        return ResponseEntity.ok().build();
    }
}

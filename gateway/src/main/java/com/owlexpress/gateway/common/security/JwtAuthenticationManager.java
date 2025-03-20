package com.owlexpress.gateway.common.security;

import com.owlexpress.gateway.common.Role;
import com.owlexpress.gateway.common.TokenStatus;
import com.owlexpress.gateway.common.TokenValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j(topic = "토큰 추출")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private static final String BEARER_PREFIX = "Bearer ";
    private final TokenValidator tokenValidator;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication)
                .map(auth ->
                        ((String) auth.getCredentials()).substring(BEARER_PREFIX.length())
                )
                // 실제 검증 후 인증 객체 생성
                .map(jwt -> {
                    Role userRole = tokenValidator.getUserRole(jwt);
                    String userId = tokenValidator.getUserId(jwt);
                    return new UsernamePasswordAuthenticationToken(
                            userId,
                            authentication.getCredentials(),
                            List.of(new SimpleGrantedAuthority("ROLE_" + userRole.getName()))
                    );
                });

    }
}

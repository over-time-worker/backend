package com.owlexpress.gateway.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.gateway.common.Passport;
import com.owlexpress.gateway.common.Role;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j(topic = "권한별 인가")
@Component
public class AddPassportHeaderGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Set<String> ALLOWED_UNAUTHORIZED_REQUEST = Set.of(
            "/api/auth/sign-in",
            "/api/users/signup"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getURI().getPath();

        // 회원가입과 로그인 과정은 인증되지 않은 상태이므로 인가 필터 거치지 X
        if (ALLOWED_UNAUTHORIZED_REQUEST.contains(path)) {
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        ServerHttpResponse response = exchange.getResponse();
                        log.info("no auth response status code = {}", response.getStatusCode());
                    }));
        }

        return getAuthentication()
                .flatMap(auth -> {
                    Long userId = (Long) auth.getPrincipal();
                    Role userRole = auth.getAuthorities().stream()
                            .map(role -> Role.from(
                                    role.getAuthority().substring("ROLE_".length())))
                            .toList()
                            .get(0);

                    Passport passport = Passport.builder()
                            .userId(userId)
                            .userRole(userRole)
                            .build();

                    try {
                        ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                                .header("X-User-Passport",
                                        objectMapper.writeValueAsString(passport))
                                .build();
                        return chain.filter(exchange.mutate().request(modifiedRequest).build());
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }
                });

    }

    private Mono<UsernamePasswordAuthenticationToken> getAuthentication() {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth instanceof UsernamePasswordAuthenticationToken)
                .cast(UsernamePasswordAuthenticationToken.class);

    }
}



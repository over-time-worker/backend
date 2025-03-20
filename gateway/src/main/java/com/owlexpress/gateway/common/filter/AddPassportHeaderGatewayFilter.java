package com.owlexpress.gateway.common.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.gateway.common.Passport;
import com.owlexpress.gateway.common.Role;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AddPassportHeaderGatewayFilter implements GlobalFilter, Ordered {

    @Override
    public int getOrder() {
        return 0;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

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



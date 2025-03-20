//package com.owlexpress.gateway.presentation;
//
//import com.owlexpress.gateway.common.TokenStatus;
//import com.owlexpress.gateway.common.TokenValidator;
//import java.util.Set;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//
//@Slf4j(topic = "JWT 검증 및 인가")
//@Component
//@RequiredArgsConstructor
//public class JwtAuthorizationFilter implements GlobalFilter {
//
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String BEARER_PREFIX = "Bearer ";
//    private final TokenValidator validator;
//
//    private final Set<String> ALLOWED_UNAUTHORIZED_REQUEST = Set.of(
//            "/api/auth/sign-in",
//            "/api/users/signup"
//    );
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String path = exchange.getRequest().getURI().getPath();
//
//        // 회원가입과 로그인 과정은 인증되지 않은 상태이므로 인가 필터 거치지 X
//        if (ALLOWED_UNAUTHORIZED_REQUEST.contains(path)) {
//            return chain.filter(exchange)
//                    .then(Mono.fromRunnable(() -> {
//                        ServerHttpResponse response = exchange.getResponse();
//                        log.info("no auth response status code = {}", response.getStatusCode());
//                    }));
//        }
//
//        // TODO : 트랜잭션 ID 추가해서 전파하는 방법도 고려 (ex.X-TRANSACTION-ID)
//        HttpHeaders headers = exchange.getRequest().getHeaders();
//        if (!headers.containsKey(AUTHORIZATION_HEADER)) {
//            throw new IllegalArgumentException("정상적인 요청이 아닙니다.");
//        }
//
//        String authorizationHeader = headers.get(AUTHORIZATION_HEADER).get(0);
//        String accessToken = authorizationHeader.replace(BEARER_PREFIX, "");
//
//        TokenStatus tokenStatus = validator.validateAccessToken(accessToken);
//
//        // TODO : EXPIRED 라면 액세스 토큰 발급 필요하다는 에러 메시지 보냄
//        if (tokenStatus != TokenStatus.VALID) {
//            throw new IllegalArgumentException("JWT 토큰이 이상합니다.");
//        }
//
//        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            ServerHttpResponse response = exchange.getResponse();
//            log.info("auth completed response status code = {}", response.getStatusCode());
//        }));
//    }
//}

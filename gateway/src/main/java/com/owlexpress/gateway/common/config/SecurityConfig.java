package com.owlexpress.gateway.common.config;

import com.owlexpress.gateway.common.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.FormLoginSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.HttpBasicSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(
            ServerHttpSecurity http,
            ReactiveAuthenticationManager jwtAuthenticationManager,
            ServerAuthenticationConverter jwtAuthenticationConverter
    ) {
        AuthenticationWebFilter authenticationWebFilter =
                new AuthenticationWebFilter(jwtAuthenticationManager);

        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationConverter);

        http.csrf(CsrfSpec::disable)
                .formLogin(FormLoginSpec::disable)
                .httpBasic(HttpBasicSpec::disable)
                .securityContextRepository(
                        NoOpServerSecurityContextRepository.getInstance()
                ); // Session - stateless

        // 생성한 필터 등록
        http.addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION);
        // 사용자
        manageUserRoute(http);
        // 관리자
        manageMasterRoute(http);
        // 상품
        manageProductRoute(http);
        // AI
        manageAiRoute(http);
        // 결제
        managePaymentRoute(http);
        // 배송
        managerDeliveryRoute(http);
        // 알림
        manageAlarmRoute(http);
        // 허브 간 이동 정보 관리
        manageHubIntervalInfoRoute(http);
        // 주문
        manageOrderRoute(http);
        // 허브 상품
        manageHubProductRoute(http);
        // 배송 담당자
        manageDeliveryManagerRoute(http);
        // 허브
        manageHubRoute(http);
        // 공급 업체
        manageProducerRoute(http);
        // 수령 업체
        manageConsumerRoute(http);
        // 장바구니
        manageCartRoute(http);

        // auth 경로는 항상 허용
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("api/auth/**").permitAll()
                .anyExchange().permitAll() // 임시 테스트용으로 풀기
        );

        return http.build();
    }

    private static void manageHubIntervalInfoRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("api/hub-interval-info/**").permitAll()
        );
    }

    private static void manageCartRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("api/carts/**")
                .hasAnyRole(Role.MASTER.getName(), Role.CONSUMER_COMPANY_MANAGER.getName())
        );
    }

    private static void manageConsumerRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, "api/consumers/**")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.HUB_MANAGER.getName(),
                        Role.HUB_DELIVERY_MANAGER.getName(),
                        Role.CONSUMER_COMPANY_MANAGER.getName()
                )

                .pathMatchers(HttpMethod.PUT, "api/consumers/**")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.PRODUCER_COMPANY_MANAGER.getName(),
                        Role.HUB_MANAGER.getName()
                )

                .pathMatchers("api/consumers/**")
                .hasAnyRole(Role.MASTER.getName(), Role.HUB_MANAGER.getName())
        );
    }

    private static void manageProducerRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, "api/producers/**")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.CONSUMER_COMPANY_MANAGER.getName(),
                        Role.HUB_DELIVERY_MANAGER.getName(),
                        Role.HUB_MANAGER.getName()
                )

                .pathMatchers(HttpMethod.PUT, "api/producers/**")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.PRODUCER_COMPANY_MANAGER.getName(),
                        Role.HUB_MANAGER.getName()
                )

                .pathMatchers("api/producers/**")
                .hasAnyRole(Role.MASTER.getName(), Role.HUB_MANAGER.getName())
        );
    }

    private static void manageHubRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, "api/hub/**").permitAll()
                .pathMatchers("api/hub/**").hasRole(Role.MASTER.getName())
        );
    }

    private static void manageDeliveryManagerRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.POST, "api/hub/delivery/**")
                .hasAnyRole(Role.MASTER.getName(), Role.HUB_DELIVERY_MANAGER.getName())

                .pathMatchers(HttpMethod.GET, "api/hub/delivery/**")
                .hasRole(Role.MASTER.getName())

                .pathMatchers("api/hub/consumer/**")
                .hasAnyRole(Role.MASTER.getName(), Role.HUB_MANAGER.getName())

                .pathMatchers(HttpMethod.PUT, "api/hub/delivery/**").hasRole(Role.MASTER.getName())
                .pathMatchers(HttpMethod.DELETE, "api/hub/delivery/**")
                .hasAnyRole(Role.MASTER.getName(), Role.HUB_MANAGER.getName())
        );
    }

    private static void manageHubProductRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, "api/hub/product/**").permitAll()
                .pathMatchers("api/hub/product/**")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.HUB_MANAGER.getName()
                )
        );
    }

    private static void manageOrderRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.PATCH, "api/orders/**")
                .hasAnyRole(Role.MASTER.getName(), Role.HUB_MANAGER.getName())

                .pathMatchers(HttpMethod.DELETE, "api/orders/**").hasRole(Role.MASTER.getName())

                .pathMatchers("api/orders/**").permitAll()
        );
    }

    private static void manageAlarmRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.POST, "/api/alarms/**").permitAll()
                .pathMatchers("api/alarms/**").hasRole(Role.MASTER.getName())
        );
    }

    private static void managerDeliveryRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("api/delivery/delivery-history/**").hasRole(Role.MASTER.getName())

                .pathMatchers("api/delivery/hub/**") // 허브 배송 시작 및 완료
                .hasAnyRole(Role.HUB_DELIVERY_MANAGER.getName(), Role.MASTER.getName())

                .pathMatchers("api/delivery/company/**")// 수령 업체 배송 시작 및 완료
                .hasAnyRole(Role.CONSUMER_COMPANY_MANAGER.getName(), Role.MASTER.getName())

                .pathMatchers("api/delivery")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.HUB_MANAGER.getName(),
                        Role.CONSUMER_COMPANY_MANAGER.getName()
                )

                .pathMatchers(HttpMethod.DELETE, "api/delivery/**")
                .hasAnyRole(Role.MASTER.getName(), Role.HUB_MANAGER.getName())

                .pathMatchers(HttpMethod.PATCH, "api/delivery/**")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.CONSUMER_COMPANY_MANAGER.getName(),
                        Role.HUB_DELIVERY_MANAGER.getName(),
                        Role.HUB_MANAGER.getName()
                )
                .pathMatchers("api/delivery/search/**").permitAll()

                .pathMatchers(HttpMethod.GET, "api/delivery/**")
                .hasAnyRole(
                        Role.MASTER.getName(),
                        Role.CONSUMER_COMPANY_MANAGER.getName(),
                        Role.HUB_DELIVERY_MANAGER.getName(),
                        Role.HUB_MANAGER.getName()
                )
        );
    }

    private static void managePaymentRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.DELETE, "api/payments/**").hasRole(Role.MASTER.getName())
                .pathMatchers("api/payments/**")
                .hasAnyRole(Role.CONSUMER_COMPANY_MANAGER.getName(), Role.MASTER.getName())
        );
    }

    private static void manageAiRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.GET, "api/ai/messages/**").hasRole(Role.MASTER.getName())
                .pathMatchers(HttpMethod.POST, "api/ai/messages/**").permitAll()
        );
    }

    private static void manageProductRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers(HttpMethod.DELETE, "/api/products/**")
                .hasAnyRole(Role.HUB_MANAGER.getName(), Role.MASTER.getName())
                .pathMatchers(HttpMethod.GET, "/api/products/**").permitAll()
                .pathMatchers("/api/products/**")
                .hasAnyRole(
                        Role.PRODUCER_COMPANY_MANAGER.getName(),
                        Role.HUB_MANAGER.getName(),
                        Role.MASTER.getName()
                )
        );
    }

    private static void manageMasterRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges -> exchanges
                .pathMatchers("/api/master/**").hasRole(Role.MASTER.getName()));
    }

    private static void manageUserRoute(ServerHttpSecurity http) {
        http.authorizeExchange(exchange -> exchange
                .pathMatchers(HttpMethod.DELETE, "/api/users/**").hasRole(Role.MASTER.getName())
                .pathMatchers(HttpMethod.PUT, "/api/users/**").hasRole(Role.MASTER.getName())
                .pathMatchers("/api/users/**").permitAll()
        );
    }
}

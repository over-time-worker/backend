package com.owlexpress.hub.infrastructure.api;

import com.owlexpress.hub.application.dto.request.DirectionsRequestDto;
import com.owlexpress.hub.application.dto.response.DirectionsResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class NaverDirectionsClient {

    @Value("${naver.client-id}")
    private String clientId;

    @Value("${naver.client-secret}")
    private String clientSecret;

    private WebClient webClient;

    /** 🔹 WebClient 초기화 메서드 */
    @PostConstruct
    private void init() {
        this.webClient = WebClient.builder()
                                  .baseUrl("https://naveropenapi.apigw.ntruss.com/map-direction/v1/driving")
                                  .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                  .defaultHeader("X-NCP-APIGW-API-KEY-ID", clientId)
                                  .defaultHeader("X-NCP-APIGW-API-KEY", clientSecret)
                                  .build();
    }

    /** 🔹 비동기 방식으로 네이버 API 호출 */
    public Mono<DirectionsResponseDto> getDrivingRoute(DirectionsRequestDto requestDto) {
        return webClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .queryParam("start", requestDto.getStart())
                                .queryParam("goal", requestDto.getGoal())
                                .queryParam("option", requestDto.getOption())
                                .build())
                        .retrieve()
                        .bodyToMono(DirectionsResponseDto.class)
                        .onErrorResume(e ->
                             Mono.empty() // 오류 발생 시 빈 결과 반환
                        );
    }
}
package com.owlexpress.hub.infrastructure.api;

import com.owlexpress.hub.application.dto.request.DirectionsRequestDto;
import com.owlexpress.hub.application.dto.response.DirectionsResponseDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
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

    /** 🔹 비동기 방식으로 네이버 API 호출 (로깅 포함) */
    public Mono<DirectionsResponseDto> getDrivingRoute(DirectionsRequestDto requestDto) {
        log.info("[Naver Directions API] 요청 준비 - start: {}, goal: {}, option: {}",
                 requestDto.getStart(), requestDto.getGoal(), requestDto.getOption());

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("start", requestDto.getStart())
                        .queryParam("goal", requestDto.getGoal())
                        .queryParam("option", requestDto.getOption())
                        .build())
                .retrieve()
                .bodyToMono(DirectionsResponseDto.class)
                .doOnNext(response -> log.info("[Naver Directions API] 응답 수신 완료"))
                .doOnError(error -> log.error("[Naver Directions API] 오류 발생: {}", error.getMessage(), error))
                .onErrorResume(e -> {
                    log.warn("[Naver Directions API] 오류로 인해 빈 응답 반환");
                    return Mono.empty();
                });
    }
}
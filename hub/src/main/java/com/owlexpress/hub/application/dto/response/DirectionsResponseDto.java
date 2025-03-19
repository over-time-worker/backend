package com.owlexpress.hub.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectionsResponseDto {

    private Integer code; // 응답 코드
    private String message; // 응답 메시지
    private String currentDateTime; // 요청 시간

    @JsonProperty("route")
    private Map<String, List<RouteOption>> route; // 옵션별 경로 정보

    @Builder
    public DirectionsResponseDto(
            Integer code,
            String message,
            String currentDateTime,
            Map<String, List<RouteOption>> route
    ) {
        this.code = code;
        this.message = message;
        this.currentDateTime = currentDateTime;
        this.route = route;
    }

    @Getter
    @NoArgsConstructor
    public static class RouteOption {
        private Summary summary; // 경로 요약 정보

        @Builder
        public RouteOption(Summary summary) {
            this.summary = summary;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Summary {
        private StartGoal start; // 출발지 정보
        private StartGoal goal; // 목적지 정보
        private Integer distance; // 전체 경로 거리(m)
        private Integer duration; // 전체 경로 소요 시간(ms)
        private String departureTime; // 예상 출발 시간

        @Builder
        public Summary(
                StartGoal start,
                StartGoal goal,
                Integer distance,
                Integer duration,
                String departureTime
        ) {
            this.start = start;
            this.goal = goal;
            this.distance = distance;
            this.duration = duration;
            this.departureTime = departureTime;
        }

        @Getter
        @NoArgsConstructor
        public static class StartGoal {
            private List<Double> location; // [경도, 위도]
            private Integer dir; // 방향 (0: 전방, 1: 왼쪽, 2: 오른쪽)

            @Builder
            public StartGoal(
                    List<Double> location,
                    Integer dir
            ) {
                this.location = location;
                this.dir = dir;
            }
        }
    }
}
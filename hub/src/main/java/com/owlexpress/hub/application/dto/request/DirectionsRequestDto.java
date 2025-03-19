package com.owlexpress.hub.application.dto.request;

import com.owlexpress.hub.common.exception.HubIntervalInfoException;
import com.owlexpress.hub.domain.entity.Hub;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DirectionsRequestDto {
    private String start;   // 시작 좌표 (경도, 위도)
    private String goal;    // 목적지 좌표 (경도, 위도)
    private String option;  // 경로 탐색 옵션 (기본값: fastest)

    @Builder
    public DirectionsRequestDto(
            String start,
            String goal,
            String option
    ) {
        this.start = start;
        this.goal = goal;
        this.option = option != null? option: "trafast";
    }

    public static DirectionsRequestDto fromEntity(Hub startHub, Hub endHub) {
        return DirectionsRequestDto.builder()
                            .start(convertPointToString(startHub.getLocation()))
                            .goal(convertPointToString(endHub.getLocation()))
                            .option("trafast")
                            .build();
    }

    public static String convertPointToString(Point location) {
        if (location == null) {
            throw new HubIntervalInfoException.LocationNotExistException();
        }
        return location.getX() + "," + location.getY();
    }


}
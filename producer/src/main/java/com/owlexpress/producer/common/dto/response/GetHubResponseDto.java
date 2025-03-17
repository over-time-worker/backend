package com.owlexpress.producer.common.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class GetHubResponseDto {
    //TODO:: HubReponseDto 데이터 보고 똑같이 필드 맞추기
    // 그리고 필요한 데이터만 추출하는 static 메서드 구현
    private String hubAddress;
    private UUID hubManagerId;

    @Builder
    public GetHubResponseDto(
            String hubAddress,
            UUID hubManagerId
    ) {
        this.hubAddress = hubAddress;
        this.hubManagerId = hubManagerId;
    }
}

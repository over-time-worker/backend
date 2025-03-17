package com.owlexpress.producer.common.dto.response;

import lombok.Data;

@Data
public class GetUserResponseDto {
    //TODO:: userResponse 데이터 보고 똑같이 필드 맞추기
    // 그리고 필요한 데이터만 추출하는 static 메서드 구현
    private String username;
    private String userPhoneNumber;

    public GetUserResponseDto(String username) {
        this.username = username;
    }
}

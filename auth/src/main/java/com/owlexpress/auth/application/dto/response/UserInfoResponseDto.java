package com.owlexpress.auth.application.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserInfoResponseDto {

    private Long userId;
    private String role;

    // TODO: 추후에는 전화번호, 이름 같은 정보도 필요함(ex.업체 배송담당자 등록시)
}

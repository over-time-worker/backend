package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.common.constant.ApprovalStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApprovalUserRequestDto {
    private Long userId;
    private ApprovalStatus approvalStatus;

    @Builder
    public ApprovalUserRequestDto(Long userId, ApprovalStatus approvalStatus) {
        this.userId = userId;
        this.approvalStatus = approvalStatus;
    }
}

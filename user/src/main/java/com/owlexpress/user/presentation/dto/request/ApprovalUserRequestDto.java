package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.domain.constant.ApprovalStatus;
import lombok.Getter;

@Getter
public class ApprovalUserRequestDto {
    private Long userId;
    private ApprovalStatus approvalStatus;
}

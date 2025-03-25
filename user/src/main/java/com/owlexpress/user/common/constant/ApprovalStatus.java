package com.owlexpress.user.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ApprovalStatus {
    APPROVED("approved"),
    REJECTED("rejected"),
    PENDING("pending");

    private final String name;
}

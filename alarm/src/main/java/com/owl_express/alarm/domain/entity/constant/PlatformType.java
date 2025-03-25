package com.owl_express.alarm.domain.entity.constant;

import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedPlatformTypeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PlatformType{
    SLACK("slack");

    private final String value;

    public static PlatformType getType(String type) {
        for(PlatformType pt : PlatformType.values()) {
            if(pt.value.equalsIgnoreCase(type)) {
                return pt;
            }
        }
        throw new NotSupportedPlatformTypeException("지원하지 않는 플랫폼 타입 입니다." + type);
    }
}

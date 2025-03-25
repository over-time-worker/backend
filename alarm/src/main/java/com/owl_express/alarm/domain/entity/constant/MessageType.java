package com.owl_express.alarm.domain.entity.constant;

import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedPlatformTypeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MessageType{
    NORMAL("일반 메세지"),
    RESERVATION("예약 메세지");

    private final String value;

    public static MessageType getType(String type) {
        for(MessageType mt : MessageType.values()) {
            if(mt.value.equalsIgnoreCase(type)) {
                return mt;
            }
        }
        throw new NotSupportedPlatformTypeException("지원하지 않는 메세지 타입 입니다." + type);
    }
}

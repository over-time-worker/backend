package com.owlexpress.delivery.domain.entity.constant;

import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedPlatformTypeException;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public enum PlatformType{
    SLACK("slack");

    private final String value;

    public static PlatformType getType(String type) {
        if(!StringUtils.hasText(type)) {
            throw new NotSupportedPlatformTypeException("플랫폼 타입이 비어있습니다.");
        }

        return Arrays.stream(PlatformType.values())
                .filter(val -> val.name().equalsIgnoreCase(type.trim()))
                .findFirst()
                .orElseThrow(() -> new NotSupportedPlatformTypeException("지원하지 않는 플랫폼 타입 입니다. : " + type));
    }
}

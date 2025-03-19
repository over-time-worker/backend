package com.owlexpress.hub.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.Duration;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {

    @Override
    public Long convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            return null;
        }
        return duration.getSeconds(); // PostgreSQL INTERVAL과 호환되는 초 단위 저장
    }

    @Override
    public Duration convertToEntityAttribute(Long seconds) {
        if (seconds == null) {
            return null;
        }
        return Duration.ofSeconds(seconds);
    }
}
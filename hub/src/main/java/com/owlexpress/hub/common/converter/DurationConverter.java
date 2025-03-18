package com.owlexpress.hub.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Converter(autoApply = true)
@Slf4j
public class DurationConverter implements AttributeConverter<Duration, String> {

    @Override
    public String convertToDatabaseColumn(Duration duration) {
        if (duration == null) {
            return null;
        }
        // PostgreSQL INTERVAL 형식으로 변환 (예: '4 hours 8 minutes 39 seconds')
        long seconds = duration.getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long remainingSeconds = seconds % 60;

        String interval = String.format("%d hours %d minutes %d seconds", hours, minutes, remainingSeconds);
        log.info("convertToDatabaseColumn: Converted Duration {} to Interval {}", duration, interval);
        return interval;
    }

    @Override
    public Duration convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        try {
            String[] parts = dbData.split(" ");
            long hours = Long.parseLong(parts[0]);
            long minutes = Long.parseLong(parts[2]);
            long seconds = Long.parseLong(parts[4]);

            Duration duration = Duration.ofSeconds(hours * 3600 + minutes * 60 + seconds);
            log.info("convertToEntityAttribute: Converted Interval {} to Duration {}", dbData, duration);
            return duration;
        } catch (Exception e) {
            log.error("convertToEntityAttribute: Error parsing Interval {}", dbData, e);
            return null;
        }
    }
}
package com.owlexpress.delivery.common.converter;

import java.time.Duration;
import org.postgresql.util.PGInterval;

public class IntervalConverter {

    public static Duration convertPGIntervalToDuration(PGInterval pgInterval) {

        long hours = pgInterval.getHours();
        long minutes = pgInterval.getMinutes();
        double seconds = pgInterval.getSeconds();

        Duration duration = Duration.ofHours(hours)
                .plusMinutes(minutes)
                .plusSeconds((long) seconds);

        return duration;
    }
}

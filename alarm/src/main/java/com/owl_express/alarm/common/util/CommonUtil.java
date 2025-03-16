package com.owl_express.alarm.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommonUtil {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String LocalDateTimetoString(LocalDateTime localDateTime) {

        return localDateTime == null ? null : localDateTime.format(dateTimeFormatter);
    }

}

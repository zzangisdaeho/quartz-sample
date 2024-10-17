package com.autocrypt.safe_no.safe_no.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {

    // ZonedDateTime을 "yyyyMMddHHmmss" 형식으로 변환하는 메서드
    public static String formatZonedDateTime(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return zonedDateTime.format(formatter);
    }

    public static ZonedDateTime switchZoneToKorea(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
    }
}

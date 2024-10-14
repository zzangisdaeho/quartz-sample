package com.autocrypt.safeno.safeno.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SafeNoUtil {

    // 숫자만 남긴 번호
    public static String getTelNoNumberOnly(String telNo) {
        return telNo.replaceAll("[^0-9]", "");
    }

    // ZonedDateTime을 "yyyyMMddHHmmss" 형식으로 변환하는 메서드
    public static String formatZonedDateTime(ZonedDateTime zonedDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return zonedDateTime.format(formatter);
    }
}

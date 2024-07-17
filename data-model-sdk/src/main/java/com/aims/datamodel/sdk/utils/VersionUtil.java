package com.aims.datamodel.sdk.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VersionUtil {
    public static String newVersionNoByNow() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        return LocalDateTime.now().format(dtf);
    }
}

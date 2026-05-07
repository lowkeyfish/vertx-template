/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Date;

public final class DateTimeUtilsEnhance {
    public static long epochSecond() {
        return OffsetDateTime.now().toEpochSecond();
    }

    public static long epochMilliSecond() {
        return OffsetDateTime.now().toInstant().toEpochMilli();
    }

    public static LocalDateTime convert(long epochMilliSecond) {
        return Instant.ofEpochMilli(epochMilliSecond)
                .atOffset(OffsetDateTime.now().getOffset())
                .toLocalDateTime();
    }

    public static long convert(LocalDateTime localDateTime) {
        return localDateTime
                .atOffset(OffsetDateTime.now().getOffset())
                .toInstant()
                .toEpochMilli();
    }

    public static Date convertToDate(LocalDateTime localDateTime) {
        return java.sql.Timestamp.valueOf(localDateTime);
    }
}

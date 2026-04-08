/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.utils;

import io.opentelemetry.api.trace.Span;

public final class OtelUtils {

    public static String getTraceId() {
        Span currentSpan = Span.current();
        if (currentSpan == null || !currentSpan.getSpanContext().isValid()) {
            return "";
        }
        return currentSpan.getSpanContext().getTraceId();
    }

    public static String getSpanId() {
        Span currentSpan = Span.current();
        if (currentSpan == null || !currentSpan.getSpanContext().isValid()) {
            return "";
        }
        return currentSpan.getSpanContext().getSpanId();
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.log4j2;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.message.ObjectMessage;

public final class DataMessage {
    private DataMessage() {}

    public static ObjectMessage of(String message) {
        return DataMessage.of(message, null);
    }

    public static ObjectMessage of(String message, Map<String, Object> data) {
        if (data == null) {
            return new ObjectMessage(ImmutableMap.of("message", message));
        }
        Map map = new HashMap<>(data);
        map.put("message", message);
        return new ObjectMessage(map);
    }
}

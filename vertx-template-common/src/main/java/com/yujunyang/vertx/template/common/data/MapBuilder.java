/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.data;

import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import java.util.HashMap;
import java.util.Map;

public final class MapBuilder {
    private Map<String, Object> map = new HashMap<>();

    public MapBuilder put(String key, Object value) {
        map.put(key, value);
        return this;
    }

    public Map<String, Object> toMap() {
        return map;
    }

    public String serializeMapToJson() {
        return JacksonUtils.serialize(map, JacksonUtils.DEFAULT_OBJECT_MAPPER);
    }
}

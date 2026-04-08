/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.environment;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yujunyang.vertx.template.common.enums.EnumUtils;
import com.yujunyang.vertx.template.common.enums.ValueDescriptionEnum;

public enum EnvironmentType implements ValueDescriptionEnum<String> {
    DEV("dev", "开发环境"),
    TEST("test", "测试环境"),
    PROD("prod", "生产环境");

    @JsonValue
    private String value;

    private String description;

    EnvironmentType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getValue() {
        return value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static EnvironmentType parse(String value) {
        return EnumUtils.getByValue(value, EnvironmentType.class);
    }
}

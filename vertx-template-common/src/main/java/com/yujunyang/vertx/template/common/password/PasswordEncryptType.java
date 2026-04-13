/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.password;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yujunyang.vertx.template.common.enums.EnumUtils;
import com.yujunyang.vertx.template.common.enums.ValueDescriptionEnum;

public enum PasswordEncryptType implements ValueDescriptionEnum<Integer> {
    ARGON2ID(1, "ARGON2ID");

    @JsonValue
    private Integer value;

    private String description;

    PasswordEncryptType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getValue() {
        return value;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static PasswordEncryptType parse(Object value) {
        return EnumUtils.getByIntValueOrStringName(value, PasswordEncryptType.class);
    }
}

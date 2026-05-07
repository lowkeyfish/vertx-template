/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yujunyang.vertx.template.common.enums.EnumUtils;
import com.yujunyang.vertx.template.common.enums.ValueDescriptionEnum;

public enum NotificationCategoryType implements ValueDescriptionEnum<Integer> {
    SIGN_UP_VERIFICATION_CODE(1, "注册验证码"),
    SIGN_IN_VERIFICATION_CODE(2, "登录验证码");

    @JsonValue
    private Integer value;

    private String description;

    NotificationCategoryType(Integer value, String description) {
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
    public static NotificationCategoryType parse(Integer value) {
        return EnumUtils.getByValue(value, NotificationCategoryType.class);
    }
}

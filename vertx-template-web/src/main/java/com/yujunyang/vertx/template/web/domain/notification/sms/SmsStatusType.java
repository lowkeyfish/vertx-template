/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yujunyang.vertx.template.common.enums.EnumUtils;
import com.yujunyang.vertx.template.common.enums.ValueDescriptionEnum;

public enum SmsStatusType implements ValueDescriptionEnum<Integer> {
    PENDING(1, "未发送"),
    SENT(2, "已发送"),
    SEND_FAILED(3, "发送失败"),
    INTERCEPTED(100, "被拦截");

    @JsonValue
    private int value;

    private String description;

    SmsStatusType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SmsStatusType parse(Integer value) {
        return EnumUtils.getByValue(value, SmsStatusType.class);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}

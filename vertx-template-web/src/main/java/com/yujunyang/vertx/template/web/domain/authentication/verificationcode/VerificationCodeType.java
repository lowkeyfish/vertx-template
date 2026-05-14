/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.authentication.verificationcode;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yujunyang.vertx.template.common.enums.EnumUtils;
import com.yujunyang.vertx.template.common.enums.ValueDescriptionEnum;

public enum VerificationCodeType implements ValueDescriptionEnum<Integer> {
    SIGN_UP(1, "注册验证码"),
    SIGN_IN(2, "登录验证码");

    @JsonValue
    private Integer value;

    private String description;

    VerificationCodeType(Integer value, String description) {
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
    public static VerificationCodeType parse(Integer value) {
        return EnumUtils.getByValue(value, VerificationCodeType.class);
    }
}

package com.yujunyang.vertx.template.common.password;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.yujunyang.vertx.template.common.enums.EnumUtils;
import com.yujunyang.vertx.template.common.enums.ValueDescriptionEnum;

public enum PasswordEncodeType implements ValueDescriptionEnum<Integer> {
    AES(10, "AES"),
    ARGON2ID(1, "ARGON2ID");

    @JsonValue
    private Integer value;

    private String description;

    PasswordEncodeType(Integer value, String description) {
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
    public static PasswordEncodeType parse(Object value) {
        return EnumUtils.getByIntValueOrStringName(value, PasswordEncodeType.class);
    }
}

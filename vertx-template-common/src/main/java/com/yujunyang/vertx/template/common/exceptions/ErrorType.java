package com.yujunyang.vertx.template.common.exceptions;

public enum ErrorType {
    VALIDATION_FAILED(400),
    VALIDATION_PASSWORD_FORMAT_INVALID(400),
    AUTHENTICATION_FAILED(401),
    AUTHORIZATION_FAILED(403),
    RESOURCE_NOT_FOUND(404),
    VERSION_CONFLICT(409),
    ENTERPRISE_NAME_EXISTS(422),
    RATE_LIMIT_EXCEEDED(429),
    VERIFICATION_CODE_SEND_LIMIT_EXCEEDED(429),
    INTERNAL_SERVER_ERROR(500);

    private int code;

    ErrorType(int httpStatusCode) {
        this.code = httpStatusCode;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return this.name();
    }
}

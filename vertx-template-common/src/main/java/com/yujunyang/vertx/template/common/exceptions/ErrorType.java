package com.yujunyang.vertx.template.common.exceptions;

public enum ErrorType {
    VALIDATION_FAILED(400),
    VALIDATION_PASSWORD_FORMAT_INVALID(400),
    AUTHENTICATION_FAILED(401),
    AUTHORIZATION_FAILED(403);

    private int httpStatusCode;

    ErrorType(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public int getCode() {
        return httpStatusCode;
    }

    public String getErrorCode() {
        return this.name();
    }
}

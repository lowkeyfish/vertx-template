/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.exceptions;

import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class AppException extends RuntimeException {
    private int httpStausCode = 400;
    private ExceptionCodeType code = ExceptionCodeType.BAD_REQUEST;
    private Map<String, Object> details = new HashMap<>();

    public AppException(String message) {
        super(message);
    }

    public AppException(String message, Map<String, Object> details) {
        super(message);
        this.details = details;
    }

    public AppException(String message, ExceptionCodeType code) {
        super(message);
        this.code = code;
    }

    public AppException(String message, ExceptionCodeType code, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace,
            ExceptionCodeType code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public ExceptionCodeType getCode() {
        return code;
    }

    public String getCodeText() {
        return code.name();
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public int getHttpStausCode() {
        return httpStausCode;
    }

    public String detailedMessage() {
        return MessageFormat.format(
                "code({0}),message({1}),details({2}),httpStatusCode({3})",
                code.name(), super.getMessage(), JacksonUtils.serialize(getDetails()), httpStausCode);
    }

    public AppException changeStatusCode(int httpStausCode) {
        this.httpStausCode = httpStausCode;
        return this;
    }
}

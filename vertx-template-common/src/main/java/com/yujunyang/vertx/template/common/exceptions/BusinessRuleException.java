/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.exceptions;

import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class BusinessRuleException extends RuntimeException {
    private String code = "ERROR";
    private Map<String, Object> details = new HashMap<>();

    public BusinessRuleException(String message) {
        super(message);
    }

    public BusinessRuleException(String message, Map<String, Object> details) {
        super(message);
        this.details = details;
    }

    public BusinessRuleException(String message, String code) {
        super(message);
        this.code = code;
    }

    public BusinessRuleException(String message, String code, Map<String, Object> details) {
        super(message);
        this.code = code;
        this.details = details;
    }

    public BusinessRuleException(Throwable cause) {
        super(cause);
    }

    public BusinessRuleException(
            String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, String code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public String detailedMessage() {
        return MessageFormat.format(
                "code({0}),message({1}),,details({2})", code, super.getMessage(), JacksonUtils.serialize(getDetails()));
    }
}

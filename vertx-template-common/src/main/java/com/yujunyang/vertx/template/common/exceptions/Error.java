/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

/** 这个Error结构用于给调用方返回输出 { code, error: { type, message, details: [ { field, message, extra: {} } ] } } */
@JsonInclude(Include.NON_NULL)
public class Error {
    private ErrorType type;
    private String message;
    private List<ErrorDetail> details;

    public Error(ErrorType type, String message, List<ErrorDetail> details) {
        this.type = type;
        this.message = message;
        this.details = details;
    }

    public int getCode() {
        return type.getCode();
    }

    public String getType() {
        return type.getType();
    }

    public String getMessage() {
        return message;
    }

    public List<ErrorDetail> getDetails() {
        return details;
    }
}

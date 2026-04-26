/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.exceptions;

import java.util.List;
import java.util.Map;

/** 用于系统主动抛出的业务规则相关的异常 此类异常是因为用户输入或操作不符合业务规则导致，不需要记录错误日志 */
public class BusinessException extends AbstractException {
    public BusinessException(String message, ErrorType errorType) {
        super(message, errorType, null, null, null);
    }

    public BusinessException(String message, ErrorType errorType, List<ErrorDetail> errorDetails) {
        super(message, errorType, errorDetails, null, null);
    }

    public BusinessException(String message, ErrorType errorType, Map<String, Object> context) {
        super(message, errorType, null, context, null);
    }

    public BusinessException(
            String message, ErrorType errorType, List<ErrorDetail> errorDetails, Map<String, Object> context) {
        super(message, errorType, errorDetails, context, null);
    }

    public BusinessException(
            String message,
            ErrorType errorType,
            List<ErrorDetail> errorDetails,
            Map<String, Object> context,
            Throwable cause) {
        super(message, errorType, errorDetails, context, cause);
    }
}

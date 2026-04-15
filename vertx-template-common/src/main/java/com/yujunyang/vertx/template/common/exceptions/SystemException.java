package com.yujunyang.vertx.template.common.exceptions;

import java.util.List;
import java.util.Map;

public class SystemException extends AbstractException {
    public SystemException(String message) {
        this(message, null, null, null);
    }

    public SystemException(String message, List<ErrorDetail> errorDetails) {
        this(message, errorDetails, null, null);
    }

    public SystemException(String message, Map<String, Object> context) {
        this(message, null, context, null);
    }

    public SystemException(String message, List<ErrorDetail> errorDetails, Map<String, Object> context) {
        this(message, errorDetails, context, null);
    }

    public SystemException(
            String message, List<ErrorDetail> errorDetails, Map<String, Object> context, Throwable cause) {
        super(message, ErrorType.INTERNAL_SERVER_ERROR, errorDetails, context, cause);
    }
}

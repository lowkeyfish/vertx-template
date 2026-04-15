package com.yujunyang.vertx.template.common.exceptions;

import java.util.List;
import java.util.Map;

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

package com.yujunyang.vertx.template.common.exceptions;

import java.util.List;
import java.util.Map;

/** 主要用于非业务规则导致的异常，需要记录错误日志 单独自定义是为了使用上可以更方便，且附带更多的异常信息 */
public class SystemException extends AbstractException {
    public SystemException(String message) {
        super(message, ErrorType.INTERNAL_SERVER_ERROR, null, null, null);
    }

    public SystemException(String message, ErrorType errorType) {
        super(message, errorType, null, null, null);
    }

    public SystemException(String message, Throwable cause) {
        super(message, ErrorType.INTERNAL_SERVER_ERROR, null, null, cause);
    }

    public SystemException(String message, Map<String, Object> context) {
        super(message, ErrorType.INTERNAL_SERVER_ERROR, null, context, null);
    }

    public SystemException(String message, ErrorType errorType, Throwable cause) {
        super(message, errorType, null, null, cause);
    }

    public SystemException(
            String message,
            ErrorType errorType,
            List<ErrorDetail> errorDetails,
            Map<String, Object> context,
            Throwable cause) {
        super(message, errorType, errorDetails, context, cause);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String message;
        private ErrorType errorType;
        private List<ErrorDetail> errorDetails;
        private Map<String, Object> context;
        private Throwable cause;

        private Builder() {
            this.errorType = ErrorType.INTERNAL_SERVER_ERROR;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder errorType(ErrorType errorType) {
            this.errorType = errorType;
            return this;
        }

        public Builder errorDetails(List<ErrorDetail> errorDetails) {
            this.errorDetails = errorDetails;
            return this;
        }

        public Builder context(Map<String, Object> context) {
            this.context = context;
            return this;
        }

        public Builder cause(Throwable cause) {
            this.cause = cause;
            return this;
        }

        public SystemException build() {
            return new SystemException(message, errorType, errorDetails, context, cause);
        }
    }
}

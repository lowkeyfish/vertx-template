package com.yujunyang.vertx.template.common.exceptions;

import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import java.util.List;
import java.util.Map;

public abstract class AbstractException extends RuntimeException {
    private Error error;
    private Map<String, Object> context;

    public AbstractException(
            String message,
            ErrorType errorType,
            List<ErrorDetail> errorDetails,
            Map<String, Object> context,
            Throwable cause) {
        super(message, cause);
        this.error = new Error(errorType, message, errorDetails);
        this.context = context;
    }

    public Error getError() {
        return error;
    }

    public Map<String, Object> getContext() {
        return context;
    }

    public String getLogMessage() {
        return JacksonUtils.serialize(Map.of("error", error, "context", context));
    }
}

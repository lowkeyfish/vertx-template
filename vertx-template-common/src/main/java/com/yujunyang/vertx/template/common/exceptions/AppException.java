package com.yujunyang.vertx.template.common.exceptions;

import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class AppException extends RuntimeException {
    private ErrorType error;
    private String userMessage;
    private Map<String, Object> details = new HashMap<>();

    public AppException(ErrorType error, String message) {
        super(message);
        this.userMessage = message;
        this.error = error;
    }

    public AppException(ErrorType error, String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
        this.error = error;
    }

    public AppException(ErrorType error, String message, Map<String, Object> details) {
        super(message);
        this.userMessage = message;
        this.error = error;
        this.details = details;
    }

    public AppException(ErrorType error, String message, Map<String, Object> details, String userMessage) {
        super(message);
        this.userMessage = userMessage;
        this.error = error;
        this.details = details;
    }

    public ErrorType getError() {
        return error;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String detailedMessage() {
        return MessageFormat.format(
                "code({0}),errorCode({1}),message({2}),details({3}),userMessage({4})",
                error.getCode(),
                error.getErrorCode(),
                super.getMessage(),
                JacksonUtils.serialize(getDetails()),
                userMessage);
    }
}

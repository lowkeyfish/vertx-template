package com.yujunyang.vertx.template.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

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

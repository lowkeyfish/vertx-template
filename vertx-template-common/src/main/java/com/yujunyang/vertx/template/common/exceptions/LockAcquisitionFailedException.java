package com.yujunyang.vertx.template.common.exceptions;

public class LockAcquisitionFailedException extends RuntimeException {

    public LockAcquisitionFailedException() {}

    public LockAcquisitionFailedException(String message) {
        super(message);
    }
}

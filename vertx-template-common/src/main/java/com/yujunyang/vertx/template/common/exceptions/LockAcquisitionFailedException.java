/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.exceptions;

public class LockAcquisitionFailedException extends RuntimeException {

    public LockAcquisitionFailedException() {}

    public LockAcquisitionFailedException(String message) {
        super(message);
    }
}

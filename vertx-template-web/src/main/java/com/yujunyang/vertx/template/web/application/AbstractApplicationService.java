/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.application;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.LockAcquisitionFailedException;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import io.vertx.core.Future;
import java.util.function.Supplier;
import org.apache.logging.log4j.Logger;

public abstract class AbstractApplicationService {
    private Logger logger;

    protected AbstractApplicationService(Logger logger) {
        this.logger = logger;
    }

    protected <T> Future<T> nonnullCommand(T command) {
        if (command == null) {
            return Future.failedFuture(new SystemException("command不能为null"));
        }
        return Future.succeededFuture(command);
    }

    protected <T> Future<T> recoverLockAcquisitionFailed(Throwable t, Supplier<BusinessException> exceptionSupplier) {
        if (t instanceof LockAcquisitionFailedException) {
            logger.warn(t.getMessage());
            return Future.failedFuture(exceptionSupplier.get());
        }
        return Future.failedFuture(t);
    }
}

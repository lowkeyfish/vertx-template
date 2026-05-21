/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.vertx.handler;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.Error;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.vertx.RoutingContextUtils;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestfulFailureHandler implements Handler<RoutingContext> {
    private static final Logger LOGGER = LogManager.getLogger(RestfulFailureHandler.class);

    @Override
    public void handle(RoutingContext routingContext) {
        Throwable originalFailure = routingContext.failure();
        Throwable failure = originalFailure;
        while (!(failure instanceof SystemException || failure instanceof BusinessException)
                && (failure = failure.getCause()) != null) {}

        if (!(failure instanceof SystemException || failure instanceof BusinessException)) {
            failure = originalFailure;
        }

        if (failure instanceof SystemException) {
            SystemException systemException = (SystemException) failure;
            LOGGER.error(systemException.getLogMessage(), systemException);
            RoutingContextUtils.responseFailure(routingContext, new Error(ErrorType.INTERNAL_SERVER_ERROR, "服务器内部错误"));
            return;
        }

        if (failure instanceof BusinessException) {
            BusinessException businessException = (BusinessException) failure;
            Error error = businessException.getError();
            if (error.getCode() == 401 || error.getCode() == 403) {
                routingContext.response().setStatusCode(error.getCode());
            }
            RoutingContextUtils.responseFailure(routingContext, error);
            return;
        }

        LOGGER.error(failure.getMessage(), failure);
        RoutingContextUtils.responseFailure(routingContext, new Error(ErrorType.INTERNAL_SERVER_ERROR, "服务器内部错误"));
    }
}

package com.yujunyang.vertx.template.common.vertx.handler;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.Error;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestfulFailureHandler implements Handler<RoutingContext> {
    private static final Logger LOGGER = LogManager.getLogger(RestfulFailureHandler.class);

    @Override
    public void handle(RoutingContext routingContext) {
        if (routingContext.statusCode() == 401) {
            routingContext.response().setStatusCode(401);
            routingContext.json(new JsonObject()
                    .put("code", 401)
                    .put("error", new Error(ErrorType.AUTHENTICATION_FAILED, "用户认证失败")));
            return;
        }

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
            routingContext.json(new JsonObject()
                    .put("code", 500)
                    .put("error", new Error(ErrorType.INTERNAL_SERVER_ERROR, "服务器内部错误")));
            return;
        }

        if (failure instanceof BusinessException) {
            BusinessException businessException = (BusinessException) failure;
            routingContext.json(new JsonObject()
                    .put("code", businessException.getError().getCode())
                    .put("error", businessException.getError()));
            return;
        }

        routingContext.json(
                new JsonObject().put("code", 500).put("error", new Error(ErrorType.INTERNAL_SERVER_ERROR, "服务器内部错误")));
        LOGGER.error(failure.getMessage(), failure);
    }
}

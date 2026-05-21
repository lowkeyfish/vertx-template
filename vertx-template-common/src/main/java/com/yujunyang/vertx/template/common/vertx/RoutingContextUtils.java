/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.vertx;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.Error;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.utils.OtelUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RoutingContextUtils {
    private static final Logger LOGGER = LogManager.getLogger(RoutingContextUtils.class);

    private static final String KEY_MYSQL_POOL = "MYSQL_POOL";
    private static final String KEY_JWT_AUTH = "JWT_AUTH";

    public static void putMySQLPool(RoutingContext routingContext, Pool mySQLPool) {
        routingContext.put(KEY_MYSQL_POOL, mySQLPool);
    }

    public static Pool getMySQLPool(RoutingContext routingContext) {
        return routingContext.get(KEY_MYSQL_POOL);
    }

    public static void putJWTAuth(RoutingContext routingContext, JWTAuth jwtAuth) {
        routingContext.put(KEY_JWT_AUTH, jwtAuth);
    }

    public static JWTAuth getJWTAuth(RoutingContext routingContext) {
        return routingContext.get(KEY_JWT_AUTH);
    }

    public static <T> void responseSucceeded(RoutingContext routingContext, T result) {
        routingContext.json(
                new JsonObject().put("code", 0).put("result", result).put("traceId", OtelUtils.getTraceId()));
    }

    public static void responseSucceeded(RoutingContext routingContext) {
        routingContext.json(new JsonObject().put("code", 0).put("traceId", OtelUtils.getTraceId()));
    }

    public static void responseFailure(RoutingContext routingContext, Error error) {
        routingContext.json(new JsonObject()
                .put("code", error.getCode())
                .put("error", error)
                .put("traceId", OtelUtils.getTraceId()));
    }

    public static JsonObject requestBody(RoutingContext routingContext) {
        try {
            JsonObject requestBody = routingContext.body().asJsonObject();
            if (requestBody == null) {
                throw new BusinessException("参数为空", ErrorType.VALIDATION_REQUEST_BODY_EMPTY);
            }
            return requestBody;
        } catch (Exception e) {
            LOGGER.warn("requestBody转换为JsonObject出错:{}", e.getMessage(), e);
            throw new BusinessException("参数异常", ErrorType.VALIDATION_FAILED);
        }
    }

    public static <T> T requestBody(RoutingContext routingContext, Class<T> type) {
        JsonObject requestBody = requestBody(routingContext);
        return requestBody.mapTo(type);
    }
}

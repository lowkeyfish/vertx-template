/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.vertx;

import com.yujunyang.vertx.template.common.exceptions.AppException;
import com.yujunyang.vertx.template.common.exceptions.ExceptionCodeType;
import com.yujunyang.vertx.template.common.utils.OtelUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;

public final class RoutingContextUtils {
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

    public static <T> void response(RoutingContext routingContext, T result) {
        routingContext.json(
                new JsonObject().put("code", 0).put("result", result).put("traceId", OtelUtils.getTraceId()));
    }

    public static void response(RoutingContext routingContext) {
        routingContext.json(new JsonObject().put("code", 0).put("traceId", OtelUtils.getTraceId()));
    }

    public static JsonObject requestBody(RoutingContext routingContext) {
        JsonObject requestBody = routingContext.body().asJsonObject();
        if (requestBody == null) {
            throw new AppException("参数为空", ExceptionCodeType.BAD_REQUEST);
        }
        return requestBody;
    }
}

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
import io.vertx.ext.web.RoutingContext;

public final class RoutingContextUtils {
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
        JsonObject requestBody = routingContext.body().asJsonObject();
        if (requestBody == null) {
            throw new BusinessException("参数为空", ErrorType.VALIDATION_REQUEST_BODY);
        }
        return requestBody;
    }
}

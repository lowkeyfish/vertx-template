/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.vertx.router;

import com.yujunyang.vertx.template.common.vertx.RoutingContextUtils;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class OtelHttpServerRouter {
    public void appendTo(Router router) {
        router.route().handler(BodyHandler.create());

        router.get("/").handler(routingContext -> {
            RoutingContextUtils.response(routingContext, "welcome to sparrow otel http");
        });

        router.get("/health").handler(routingContext -> RoutingContextUtils.response(routingContext, "ok"));
    }
}

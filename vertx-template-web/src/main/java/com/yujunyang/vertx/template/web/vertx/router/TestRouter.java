/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.vertx.router;

import com.yujunyang.vertx.template.common.utils.OtelUtils;
import com.yujunyang.vertx.template.common.vertx.RoutingContextUtils;
import io.reactiverse.contextual.logging.ContextualData;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestRouter {
    private static final Logger LOGGER = LogManager.getLogger(TestRouter.class);

    public void appendTo(Router router) {
        router.get("/").handler(routingContext -> {
            ContextualData.put("traceId2", OtelUtils.getTraceId());
            LOGGER.info("test router，用于检查是否记录了trace_id和span_id");
            RoutingContextUtils.response(routingContext, "welcome to sparrow api");
        });

        router.get("/test/:type").handler(ctx -> {
            RoutingContextUtils.response(ctx, ctx.pathParam("type"));
        });
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.api.web.router;

import com.yujunyang.vertx.template.common.vertx.RoutingContextUtils;
import io.vertx.ext.web.Router;

public class HealthRouter {
    public void appendTo(Router router) {
        router.get("/health").handler(rc -> {
            RoutingContextUtils.response(rc);
        });
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;

public class ClientIpHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext rc) {
        String clientIp = resolveClientIp(rc);
        rc.put("clientIp", clientIp);
        rc.request().headers().set("X-Real-IP", clientIp);
        rc.next();
    }

    private String resolveClientIp(RoutingContext routingContext) {
        HttpServerRequest request = routingContext.request();
        String remoteAddress = request.remoteAddress().host();

        String xRealIp = request.getHeader("X-Real-IP");
        if (isValidIp(xRealIp)) {
            return xRealIp;
        }

        return remoteAddress;
    }

    private boolean isValidIp(String ip) {
        return ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip);
    }
}

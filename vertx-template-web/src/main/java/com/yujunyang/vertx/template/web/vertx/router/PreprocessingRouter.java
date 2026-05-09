/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.vertx.router;

import com.yujunyang.vertx.template.common.vertx.handler.ClientIpHandler;
import com.yujunyang.vertx.template.common.vertx.handler.RestfulFailureHandler;
import com.yujunyang.vertx.template.web.di.AppComponent;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class PreprocessingRouter {
    private AppComponent appComponent;

    public PreprocessingRouter(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    public void appendTo(Router router) {
        handleClientIp(router);
        handleRequestBody(router);
        handleFailure(router);
    }

    private void handleClientIp(Router router) {
        router.route().handler(new ClientIpHandler());
    }

    private void handleRequestBody(Router router) {
        router.route().handler(BodyHandler.create());
    }

    private void handleFailure(Router router) {
        router.route().failureHandler(new RestfulFailureHandler());
    }
}

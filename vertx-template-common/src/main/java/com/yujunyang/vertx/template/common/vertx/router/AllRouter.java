package com.yujunyang.vertx.template.common.vertx.router;

import com.yujunyang.vertx.template.common.vertx.handler.AccessHandler;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class AllRouter {
    public void appendTo(Router router) {
        router.route().handler(new AccessHandler());
        router.route().handler(BodyHandler.create());
    }
}

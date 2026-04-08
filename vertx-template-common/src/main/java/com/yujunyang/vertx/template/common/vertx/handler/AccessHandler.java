package com.yujunyang.vertx.template.common.vertx.handler;

import io.vertx.core.Handler;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.RoutingContext;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ObjectMessage;

public class AccessHandler implements Handler<RoutingContext> {
    private static final Logger LOGGER = LogManager.getLogger("accessLogger");

    @Override
    public void handle(RoutingContext ctx) {
        long startTime = System.currentTimeMillis();
        ctx.addEndHandler(v -> {
            Route route = ctx.currentRoute();
            String routePath = route.getPath();
            String uri = ctx.request().uri();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            LOGGER.info(new ObjectMessage(Map.of("route", routePath, "uri", uri, "duration", duration)));
        });
        ctx.next();
    }
}

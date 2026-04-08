package com.yujunyang.vertx.template.common.vertx.handler;

import com.yujunyang.vertx.template.common.utils.OtelUtils;
import io.reactiverse.contextual.logging.ContextualData;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;

public class TraceIdHandler implements Handler<RoutingContext> {

    @Override
    public void handle(RoutingContext ctx) {
        String traceId = OtelUtils.getTraceId();
        ContextualData.put("traceId", traceId);
        ctx.next();
    }
}

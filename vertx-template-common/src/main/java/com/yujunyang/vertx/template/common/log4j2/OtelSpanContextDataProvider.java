package com.yujunyang.vertx.template.common.log4j2;

import io.opentelemetry.api.trace.Span;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.core.util.ContextDataProvider;

public class OtelSpanContextDataProvider implements ContextDataProvider {
    @Override
    public Map<String, String> supplyContextData() {
        Map<String, String> contextData = new HashMap<>();
        Span currentSpan = Span.current();
        if (currentSpan != null && currentSpan.getSpanContext().isValid()) {
            contextData.put("trace_id", currentSpan.getSpanContext().getTraceId());
            contextData.put("span_id", currentSpan.getSpanContext().getSpanId());
        }
        return contextData;
    }
}

package com.yujunyang.vertx.template.common.vertx.di;

import dagger.Module;
import dagger.Provides;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.exporter.logging.otlp.OtlpJsonLoggingSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.semconv.ServiceAttributes;
import io.vertx.core.Vertx;
import io.vertx.tracing.opentelemetry.OpenTelemetryTracingFactory;
import javax.inject.Singleton;

@Module
public class VertxModule {

    @Provides
    @Singleton
    public Vertx provideVertx() {
        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(OtlpJsonLoggingSpanExporter.create()))
                .setResource(Resource.create(Attributes.of(ServiceAttributes.SERVICE_NAME, "vertx-template")))
                .build();

        OpenTelemetry openTelemetry = OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        Vertx vertx = Vertx.builder()
                .withTracer(new OpenTelemetryTracingFactory(openTelemetry))
                .build();
        return vertx;
    }
}

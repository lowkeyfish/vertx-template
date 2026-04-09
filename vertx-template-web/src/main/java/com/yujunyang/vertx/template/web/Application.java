/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web;

import com.yujunyang.vertx.template.common.launch.ApplicationLauncher;
import com.yujunyang.vertx.template.common.vertx.config.ApplicationConfigManager;
import com.yujunyang.vertx.template.web.config.ApplicationConfig;
import com.yujunyang.vertx.template.web.vertx.verticle.HttpServerVerticle;
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
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.tracing.opentelemetry.OpenTelemetryTracingFactory;

public class Application {

    public static void main(String... args) {
        //        OtlpHttpSpanExporter spanExporter = OtlpHttpSpanExporter.builder()
        //                .setEndpoint("http://localhost:4318/v1/traces")
        //                .build();

        //        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
        //                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
        //                .setResource(Resource.create(Attributes.of(ServiceAttributes.SERVICE_NAME, "vertx-template")))
        //                .build();
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
        Future.future(promise -> ApplicationLauncher.start(vertx, ApplicationConfig.class, Application::deployVerticle)
                        .onComplete(promise))
                .onFailure(e -> handleFailure(vertx, e));
    }

    private static Future<?> deployVerticle(Vertx vertx) {
        ApplicationConfig applicationConfig = ApplicationConfigManager.get();
        DeploymentOptions deploymentOptions = new DeploymentOptions()
                .setInstances(applicationConfig.getVertx().getDeploymentInstance());
        return vertx.deployVerticle(HttpServerVerticle.class, deploymentOptions)
                .onSuccess(r -> System.out.println("Verticle全部部署成功"));
    }

    private static void handleFailure(Vertx vertx, Throwable e) {
        System.err.println(e.getMessage());
        e.printStackTrace();
        if (vertx != null) {
            vertx.close();
        }
    }
}

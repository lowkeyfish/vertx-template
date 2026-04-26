/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web;

import com.yujunyang.vertx.template.common.launch.ApplicationLauncher;
import com.yujunyang.vertx.template.web.config.ApplicationConfig;
import com.yujunyang.vertx.template.web.di.AppComponent;
import com.yujunyang.vertx.template.web.di.DaggerAppComponent;
import com.yujunyang.vertx.template.web.vertx.verticle.DaggerHttpServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.redisson.api.RedissonClient;

public class DaggerApplication {

    public static void main(String[] args) {
        AppComponent appComponent = DaggerAppComponent.create();
        Vertx vertx = appComponent.getVertx();
        Future.future(promise -> ApplicationLauncher.start(
                                vertx, ApplicationConfig.class, v -> deployVerticle(v, appComponent))
                        .onComplete(promise))
                .onSuccess(r -> {
                    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                        CountDownLatch latch = new CountDownLatch(1);
                        close(appComponent, () -> latch.countDown());
                        try {
                            if (!latch.await(30, TimeUnit.SECONDS)) {
                                System.err.println("Vertx关闭超时,JVM将退出");
                            }
                        } catch (InterruptedException e) {
                            System.err.println("ShutdownHook被中断");
                            e.printStackTrace();
                        }
                        System.out.println("ShutdownHook已完成");
                    }));
                })
                .onFailure(e -> handleFailure(e, appComponent));
    }

    private static Future<?> deployVerticle(Vertx vertx, AppComponent appComponent) {
        ApplicationConfig applicationConfig =
                appComponent.getApplicationConfigProvider().getConfig();
        DeploymentOptions deploymentOptions = new DeploymentOptions()
                .setInstances(applicationConfig.getVertx().getDeploymentInstance());

        return vertx.deployVerticle(() -> new DaggerHttpServerVerticle(appComponent), deploymentOptions)
                .onSuccess(r -> System.out.println("Verticle全部部署成功"));
    }

    private static void handleFailure(Throwable e, AppComponent appComponent) {
        Vertx vertx = appComponent.getVertx();
        if (vertx != null) {
            vertx.close().onComplete(ar -> System.out.println("Vertx关闭已完成"));
        }
        System.err.println(e.getMessage());
        e.printStackTrace();
    }

    private static void close(AppComponent appComponent, Runnable handler) {
        Vertx vertx = appComponent.getVertx();
        if (vertx != null) {
            vertx.close().onComplete(ar -> {
                System.out.println("Vertx关闭已完成");

                RedissonClient redissonClient = appComponent.getRedissonClient();
                if (redissonClient != null && !redissonClient.isShutdown()) {
                    try {
                        redissonClient.shutdown(0, 5, TimeUnit.SECONDS);
                        System.out.println("RedissonClient关闭已完成");
                    } catch (Exception e) {
                        System.err.println("RedissonClient关闭出错:" + e.getMessage());
                    }
                }

                handler.run();
            });
        } else {
            handler.run();
        }
    }
}

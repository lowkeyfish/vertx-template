/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.test;

import com.yujunyang.vertx.template.common.config.ApplicationConfigManager;
import com.yujunyang.vertx.template.common.launch.ApplicationLauncher;
import com.yujunyang.vertx.template.web.config.ApplicationConfig;
import com.yujunyang.vertx.template.web.vertx.verticle.HttpServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

public class SimpleApplication {

    public static void main(String... args) {
        Vertx vertx = Vertx.vertx();
        try {
            ApplicationLauncher.start(vertx, ApplicationConfig.class, v -> {
                        ApplicationConfig applicationConfig = ApplicationConfigManager.get();
                        DeploymentOptions deploymentOptions = new DeploymentOptions()
                                .setInstances(applicationConfig.getVertx().getDeploymentInstance());

                        return vertx.deployVerticle(HttpServerVerticle.class, deploymentOptions)
                                .onSuccess(r -> System.out.println("Verticle全部部署成功"));
                    })
                    .onFailure(e -> {
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                        vertx.close();
                    });
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            vertx.close();
        }
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.launch;

import com.github.yitter.contract.IdGeneratorOptions;
import com.github.yitter.idgen.YitIdHelper;
import com.yujunyang.vertx.template.common.environment.EnvironmentType;
import com.yujunyang.vertx.template.common.environment.EnvironmentUtils;
import com.yujunyang.vertx.template.common.log4j2.Log4j2Configurator;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import com.yujunyang.vertx.template.common.vertx.config.ApplicationConfigManager;
import com.yujunyang.vertx.template.common.vertx.config.DefaultApplicationConfig;
import com.yujunyang.vertx.template.common.vertx.config.VertxApplicationConfigurator;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.Vertx;
import java.util.function.Function;

public final class ApplicationLauncher {
    private ApplicationLauncher() {}

    public static <E1 extends DefaultApplicationConfig, E2 extends VerticleBase> Future<?> start(
            Vertx vertx, Class<E1> applicationConfigClass, Function<Vertx, Future<?>> deployVerticleHandler) {
        return start(EnvironmentUtils.getEnvironment(), vertx, applicationConfigClass, deployVerticleHandler);
    }

    public static <E1 extends DefaultApplicationConfig, E2 extends VerticleBase> Future<?> start(
            EnvironmentType environmentType,
            Vertx vertx,
            Class<E1> applicationConfigClass,
            Function<Vertx, Future<?>> deployVerticleHandler) {
        CheckUtils.notNull(environmentType, "未提供环境");
        CheckUtils.notNull(vertx, "vertx不能为null");
        CheckUtils.notNull(applicationConfigClass, "applicationConfigClass不能为null");
        CheckUtils.notNull(deployVerticleHandler, "deployVerticleHandler不能为null");

        initializeIdGenerator();
        initializeLog4j2Config(environmentType);
        initializeApplicationConfig(environmentType, vertx, applicationConfigClass);
        return deployVerticleHandler.apply(vertx);
    }

    private static void initializeIdGenerator() {
        IdGeneratorOptions options = new IdGeneratorOptions((short) 1);
        YitIdHelper.setIdGenerator(options);
    }

    private static void initializeLog4j2Config(EnvironmentType environmentType) {
        // 初始log4j2配置文件
        Log4j2Configurator.initialize(environmentType);
    }

    private static <E extends DefaultApplicationConfig> void initializeApplicationConfig(
            EnvironmentType environmentType, Vertx vertx, Class<E> applicationConfigClass) {
        // 初始应用配置文件
        ApplicationConfigManager.initialize(
                VertxApplicationConfigurator.initialize(environmentType, vertx, applicationConfigClass));
    }
}

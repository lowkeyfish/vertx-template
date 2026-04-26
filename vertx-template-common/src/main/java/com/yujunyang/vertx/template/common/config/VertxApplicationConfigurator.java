/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.config;

import com.yujunyang.vertx.template.common.environment.EnvironmentType;
import com.yujunyang.vertx.template.common.environment.EnvironmentUtils;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import java.text.MessageFormat;

public final class VertxApplicationConfigurator {
    public static <E extends DefaultApplicationConfig> E initialize(
            EnvironmentType environmentType, Vertx vertx, Class<E> applicationConfigClass) {
        CheckUtils.notNull(environmentType, "未提供环境");

        ConfigRetrieverOptions options = new ConfigRetrieverOptions();

        if (VertxApplicationConfigurator.class.getClassLoader().getResource("application.yaml") != null) {
            ConfigStoreOptions defaultApplicationConfigFileStore = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("yaml")
                    .setConfig(new JsonObject().put("path", "application.yaml"));
            options.addStore(defaultApplicationConfigFileStore);
        }

        String environmentApplicationConfigFilePath =
                MessageFormat.format("application-{0}.yaml", environmentType.getValue());
        if (VertxApplicationConfigurator.class.getClassLoader().getResource(environmentApplicationConfigFilePath)
                != null) {
            ConfigStoreOptions defaultApplicationConfigFileStore = new ConfigStoreOptions()
                    .setType("file")
                    .setFormat("yaml")
                    .setConfig(new JsonObject().put("path", environmentApplicationConfigFilePath));
            options.addStore(defaultApplicationConfigFileStore);
        }

        ConfigStoreOptions sysStore =
                new ConfigStoreOptions().setType("sys").setConfig(new JsonObject().put("hierarchical", true));
        options.addStore(sysStore);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
        JsonObject config = retriever.getConfig().await();
        return config.mapTo(applicationConfigClass);
    }

    public static <E extends DefaultApplicationConfig> E initialize(Vertx vertx, Class<E> applicationConfigClass) {
        return initialize(EnvironmentUtils.getEnvironment(), vertx, applicationConfigClass);
    }
}

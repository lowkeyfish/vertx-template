/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record VertxConfig(Integer deploymentInstance) {
    private static final int DEFAULT_DEPLOYMENT_INSTANCES = 2;

    public VertxConfig {
        if (deploymentInstance == null) {
            deploymentInstance = DEFAULT_DEPLOYMENT_INSTANCES;
        }
    }

    public static VertxConfig defaultConfig() {
        return new VertxConfig(DEFAULT_DEPLOYMENT_INSTANCES);
    }
}

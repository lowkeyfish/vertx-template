/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ServerConfig(Integer port) {
    private static final int DEFAULT_PORT = 6060;

    public ServerConfig {
        if (port == null) {
            port = DEFAULT_PORT;
        }
    }

    public static ServerConfig defaultConfig() {
        return new ServerConfig(DEFAULT_PORT);
    }
}

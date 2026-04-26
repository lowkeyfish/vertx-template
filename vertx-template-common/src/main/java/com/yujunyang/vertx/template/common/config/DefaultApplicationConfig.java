/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultApplicationConfig {
    @JsonProperty("vertx")
    private VertxConfig vertx;

    @JsonProperty("server")
    private ServerConfig server;

    @JsonProperty("datasource")
    private DatasourceConfig datasource;

    @JsonProperty("redis")
    private RedisConfig redis;

    public VertxConfig getVertx() {
        return vertx;
    }

    public void setVertx(VertxConfig vertx) {
        this.vertx = vertx;
    }

    public ServerConfig getServer() {
        return server;
    }

    public void setServer(ServerConfig server) {
        this.server = server;
    }

    public DatasourceConfig getDatasource() {
        return datasource;
    }

    public void setDatasource(DatasourceConfig datasource) {
        this.datasource = datasource;
    }

    public RedisConfig getRedis() {
        return redis;
    }

    public void setRedis(RedisConfig redis) {
        this.redis = redis;
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import org.apache.commons.lang3.ObjectUtils;

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

    @JsonProperty("jwt")
    private JWTConfig jwt;

    @JsonProperty("rabbitMQ")
    private RabbitMQConfig rabbitMQ;

    @JsonProperty("security")
    private SecurityConfig security;

    public VertxConfig vertx() {
        return ObjectUtils.getIfNull(vertx, VertxConfig.defaultConfig());
    }

    public void setVertx(VertxConfig vertx) {
        this.vertx = vertx;
    }

    public ServerConfig server() {
        return ObjectUtils.getIfNull(server, ServerConfig.defaultConfig());
    }

    public void setServer(ServerConfig server) {
        this.server = server;
    }

    public DatasourceConfig datasource() {
        CheckUtils.notNull(datasource, new SystemException("配置文件缺少配置项[datasource]", ErrorType.CONFIG_ERROR));
        return datasource;
    }

    public void setDatasource(DatasourceConfig datasource) {
        this.datasource = datasource;
    }

    public RedisConfig redis() {
        CheckUtils.notNull(redis, new SystemException("配置文件缺少配置项[redis]", ErrorType.CONFIG_ERROR));
        return redis;
    }

    public void setRedis(RedisConfig redis) {
        this.redis = redis;
    }

    public JWTConfig jwt() {
        CheckUtils.notNull(jwt, new SystemException("配置文件缺少配置项[jwt]", ErrorType.CONFIG_ERROR));
        return jwt;
    }

    public void setJwt(JWTConfig jwt) {
        this.jwt = jwt;
    }

    public RabbitMQConfig rabbitMQ() {
        CheckUtils.notNull(rabbitMQ, new SystemException("配置文件缺少配置项[rabbitMQ]", ErrorType.CONFIG_ERROR));
        return rabbitMQ;
    }

    public void setRabbitMQ(RabbitMQConfig rabbitMQ) {
        this.rabbitMQ = rabbitMQ;
    }

    public SecurityConfig security() {
        CheckUtils.notNull(security, new SystemException("配置文件缺少配置项[security]", ErrorType.CONFIG_ERROR));
        return security;
    }

    public void setSecurity(SecurityConfig security) {
        this.security = security;
    }
}

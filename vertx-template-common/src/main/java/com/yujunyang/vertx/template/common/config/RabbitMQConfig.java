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

@JsonIgnoreProperties(ignoreUnknown = true)
public record RabbitMQConfig(
        @JsonProperty("host") String host,
        @JsonProperty("port") Integer port,
        @JsonProperty("username") String username,
        @JsonProperty("password") String password) {

    @Override
    public String host() {
        CheckUtils.notBlank(host, new SystemException("配置文件缺少配置项[rabbitMQ.host]", ErrorType.CONFIG_ERROR));
        return host;
    }

    @Override
    public Integer port() {
        CheckUtils.notNull(port, new SystemException("配置文件缺少配置项[rabbitMQ.port]", ErrorType.CONFIG_ERROR));
        return port;
    }

    @Override
    public String username() {
        CheckUtils.notBlank(username, new SystemException("配置文件缺少配置项[rabbitMQ.username]", ErrorType.CONFIG_ERROR));
        return username;
    }

    @Override
    public String password() {
        CheckUtils.notBlank(password, new SystemException("配置文件缺少配置项[rabbitMQ.password]", ErrorType.CONFIG_ERROR));
        return password;
    }
}

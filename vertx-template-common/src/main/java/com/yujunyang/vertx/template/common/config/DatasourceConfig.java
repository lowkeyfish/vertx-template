/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.CheckUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatasourceConfig(String host, String user, String password, Integer port, String database) {

    @Override
    public String host() {
        CheckUtils.notBlank(host, new SystemException("配置文件缺少配置项[datasource.host]", ErrorType.CONFIG_ERROR));
        return host;
    }

    @Override
    public String user() {
        CheckUtils.notBlank(user, new SystemException("配置文件缺少配置项[datasource.user]", ErrorType.CONFIG_ERROR));
        return user;
    }

    @Override
    public String password() {
        CheckUtils.notBlank(password, new SystemException("配置文件缺少配置项[datasource.password]", ErrorType.CONFIG_ERROR));
        return password;
    }

    @Override
    public Integer port() {
        CheckUtils.notNull(port, new SystemException("配置文件缺少配置项[datasource.port]", ErrorType.CONFIG_ERROR));
        return port;
    }

    @Override
    public String database() {
        CheckUtils.notBlank(database, new SystemException("配置文件缺少配置项[datasource.database]", ErrorType.CONFIG_ERROR));
        return database;
    }
}

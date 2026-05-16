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
public record JWTConfig(JWTConfigItem access, JWTConfigItem refresh, JWTConfigItem temp) {

    @Override
    public JWTConfigItem access() {
        CheckUtils.notNull(access, new SystemException("配置文件缺少配置项[jwt.access]", ErrorType.CONFIG_ERROR));
        return access;
    }

    @Override
    public JWTConfigItem refresh() {
        CheckUtils.notNull(refresh, new SystemException("配置文件缺少配置项[jwt.refresh]", ErrorType.CONFIG_ERROR));
        return refresh;
    }

    @Override
    public JWTConfigItem temp() {
        CheckUtils.notNull(temp, new SystemException("配置文件缺少配置项[jwt.temp]", ErrorType.CONFIG_ERROR));
        return temp;
    }
}

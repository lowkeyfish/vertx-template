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
public record JWTConfig(String secret, String algorithm) {

    @Override
    public String secret() {
        CheckUtils.notBlank(secret, new SystemException("配置文件缺少配置项[jwt.secret]", ErrorType.CONFIG_ERROR));
        return secret;
    }

    @Override
    public String algorithm() {
        CheckUtils.notBlank(algorithm, new SystemException("配置文件缺少配置项[jwt.algorithm]", ErrorType.CONFIG_ERROR));
        return algorithm;
    }
}

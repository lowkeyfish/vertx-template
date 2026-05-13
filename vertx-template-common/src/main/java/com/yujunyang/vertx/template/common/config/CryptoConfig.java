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
public record CryptoConfig(String aesPassword) {

    @Override
    public String aesPassword() {
        CheckUtils.notBlank(
                aesPassword, new SystemException("配置文件缺少配置项[security.crypto.aesPassword]", ErrorType.CONFIG_ERROR));
        return aesPassword;
    }
}

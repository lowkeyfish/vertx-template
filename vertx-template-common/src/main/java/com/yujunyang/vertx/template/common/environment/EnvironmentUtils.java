/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.environment;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public final class EnvironmentUtils {

    private static final String ENV_PROPERTY_KEY = "env";

    public static EnvironmentType getEnvironment() {
        String env = System.getProperty(ENV_PROPERTY_KEY);
        if (StringUtils.isEmpty(env)) {
            return EnvironmentType.DEV;
        }

        return ObjectUtils.getIfNull(EnvironmentType.parse(env.toLowerCase()), EnvironmentType.DEV);
    }
}

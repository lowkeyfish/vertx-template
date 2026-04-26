/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.config;

import com.yujunyang.vertx.template.common.utils.CheckUtils;

public final class ApplicationConfigProviderImpl implements ApplicationConfigProvider {
    private DefaultApplicationConfig applicationConfig;

    public ApplicationConfigProviderImpl(DefaultApplicationConfig applicationConfig) {
        CheckUtils.notNull(applicationConfig, "applicationConfig必须不为null");
        this.applicationConfig = applicationConfig;
    }

    public <T extends DefaultApplicationConfig> T getConfig() {
        return (T) applicationConfig;
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.vertx.config;

import com.yujunyang.vertx.template.common.utils.CheckUtils;

public final class ApplicationConfigManager {
    private static DefaultApplicationConfig CONFIG;

    private ApplicationConfigManager() {}

    public static <T extends DefaultApplicationConfig> void initialize(T config) {
        CheckUtils.notNull(config, "config不能为null");
        CheckUtils.isTrue(CONFIG == null, "配置已初始化过");
        CONFIG = config;
    }

    public static <T extends DefaultApplicationConfig> T get() {
        CheckUtils.notNull(CONFIG, "配置尚未初始化,先调用initialize(T config)完成初始化");
        return (T) CONFIG;
    }
}

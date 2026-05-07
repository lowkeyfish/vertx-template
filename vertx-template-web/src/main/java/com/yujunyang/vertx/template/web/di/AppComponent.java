/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import dagger.Component;
import io.vertx.core.Vertx;
import javax.inject.Singleton;
import org.redisson.api.RedissonClient;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    // 不再使用子组件，因为使用子组件提供ApplicationService很麻烦，还要自己保持子组件的引用才能避免每次create
    // ServiceComponent.Factory serviceComponent();

    Vertx getVertx();

    ApplicationConfigProvider getApplicationConfigProvider();

    RedissonClient getRedissonClient();
}

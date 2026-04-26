/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.common.config.di.ApplicationConfigModule;
import com.yujunyang.vertx.template.common.db.di.DatabaseModule;
import com.yujunyang.vertx.template.common.password.di.PasswordModule;
import com.yujunyang.vertx.template.common.redis.di.RedisModule;
import com.yujunyang.vertx.template.common.vertx.di.VertxModule;
import com.yujunyang.vertx.template.web.infrastructure.event.di.EventModule;
import dagger.Module;

@Module(
        includes = {
            VertxModule.class,
            ApplicationConfigModule.class,
            DatabaseModule.class,
            RedisModule.class,
            PasswordModule.class,
            EventModule.class,
            DataAccessorModule.class,
            RepositoryModule.class,
            ServiceModule.class
        })
public class AppModule {}

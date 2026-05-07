/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.common.authentication.di.AuthenticationModule;
import com.yujunyang.vertx.template.common.config.di.ApplicationConfigModule;
import com.yujunyang.vertx.template.common.db.di.DatabaseModule;
import com.yujunyang.vertx.template.common.password.di.PasswordModule;
import com.yujunyang.vertx.template.common.redis.di.RedisModule;
import com.yujunyang.vertx.template.common.vertx.di.VertxModule;
import com.yujunyang.vertx.template.web.infrastructure.di.InfrastructureModule;
import dagger.Module;

@Module(
        includes = {
            VertxModule.class,
            ApplicationConfigModule.class,
            DatabaseModule.class,
            RedisModule.class,
            PasswordModule.class,
            AliyunModule.class,
            AuthenticationModule.class,
            InfrastructureModule.class
        })
public class AppModule {}

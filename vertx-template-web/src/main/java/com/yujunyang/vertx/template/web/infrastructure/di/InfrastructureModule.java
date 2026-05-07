/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.di;

import com.yujunyang.vertx.template.web.infrastructure.event.di.EventModule;
import com.yujunyang.vertx.template.web.infrastructure.notification.di.NotificationModule;
import dagger.Module;

@Module(includes = {EventModule.class, NotificationModule.class})
public abstract class InfrastructureModule {}

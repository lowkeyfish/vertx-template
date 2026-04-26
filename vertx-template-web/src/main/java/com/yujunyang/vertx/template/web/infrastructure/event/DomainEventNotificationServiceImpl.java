/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import com.yujunyang.vertx.template.common.event.notification.DomainEventNotificationService;
import io.vertx.core.Future;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DomainEventNotificationServiceImpl implements DomainEventNotificationService {

    @Inject
    public DomainEventNotificationServiceImpl() {}

    @Override
    public <T extends DomainEvent> Future<Void> publishNotifications(Class<T> domainEventClass, int limit) {
        return null;
    }
}

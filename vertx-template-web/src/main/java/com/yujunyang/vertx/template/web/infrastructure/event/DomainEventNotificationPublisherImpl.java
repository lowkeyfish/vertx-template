/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import com.yujunyang.vertx.template.common.event.notification.DomainEventNotificationPublisher;
import io.vertx.core.Future;

public class DomainEventNotificationPublisherImpl implements DomainEventNotificationPublisher {

    @Override
    public <T extends DomainEvent> Future<Boolean> publishNotifications(Class<T> domainEventClass, int limit) {
        return Future.succeededFuture(false);
    }
}

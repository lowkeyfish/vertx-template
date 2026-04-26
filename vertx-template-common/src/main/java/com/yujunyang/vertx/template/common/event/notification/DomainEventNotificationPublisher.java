/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event.notification;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import io.vertx.core.Future;

public interface DomainEventNotificationPublisher {
    <T extends DomainEvent> Future<Boolean> publishNotifications(Class<T> domainEventClass, int limit);
}

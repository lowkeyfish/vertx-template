/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event.notification;

import com.yujunyang.vertx.template.common.event.DomainEvent;

public interface NotificationPublisher {
    <T extends DomainEvent> boolean publishNotifications(Class<T> domainEventClass, int limit);
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event;

public interface DomainEvent {

    long getTimestamp();

    default String notificationType() {
        return this.getClass().getSimpleName();
    }

    default String notificationExchange() {
        throw new UnsupportedOperationException();
    }

    String notificationRoutingKey();

    String storedEventKey();

    default String storedEventType() {
        return this.getClass().getName();
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event.notification;

import com.yujunyang.vertx.template.common.event.DomainEvent;

public class Notification {
    private DomainEvent event;
    private long notificationId;

    public Notification(long notificationId, DomainEvent event) {
        this.notificationId = notificationId;
        this.event = event;
    }

    public DomainEvent getEvent() {
        return event;
    }

    public long getNotificationId() {
        return notificationId;
    }
}

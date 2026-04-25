package com.yujunyang.vertx.template.common.event.notification;

import com.yujunyang.vertx.template.common.event.DomainEvent;

public class DomainEventNotification {
    private DomainEvent event;
    private long notificationId;

    public DomainEventNotification(long notificationId, DomainEvent event) {
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

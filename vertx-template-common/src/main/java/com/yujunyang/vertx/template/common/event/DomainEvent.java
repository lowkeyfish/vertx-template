package com.yujunyang.vertx.template.common.event;

public interface DomainEvent {

    long getTimestamp();

    String eventKey();

    default String notificationType() {
        return this.getClass().getSimpleName();
    }

    default String notificationExchange() {
        throw new UnsupportedOperationException();
    }

    String notificationRoutingKey();

    default String storedEventType() {
        return this.getClass().getName();
    }
}

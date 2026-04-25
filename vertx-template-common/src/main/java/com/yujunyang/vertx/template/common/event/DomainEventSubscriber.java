package com.yujunyang.vertx.template.common.event;

public interface DomainEventSubscriber<T> {

    public void handleEvent(final T aDomainEvent);

    public Class<T> subscribedToEventType();
}

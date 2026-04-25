package com.yujunyang.vertx.template.web.infrastructure.event;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import com.yujunyang.vertx.template.common.event.DomainEventSubscriber;
import com.yujunyang.vertx.template.common.event.EventStore;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DomainEventSubscriberImpl implements DomainEventSubscriber<DomainEvent> {
    private EventStore eventStore;

    @Inject
    public DomainEventSubscriberImpl(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public void handleEvent(DomainEvent aDomainEvent) {}

    @Override
    public Class subscribedToEventType() {
        return DomainEvent.class;
    }
}

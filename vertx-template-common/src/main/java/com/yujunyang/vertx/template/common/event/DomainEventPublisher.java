package com.yujunyang.vertx.template.common.event;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.spi.context.storage.AccessMode;
import io.vertx.core.spi.context.storage.ContextLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DomainEventPublisher {
    private static final ContextLocal<DomainEventPublisher> CONTEXT_LOCAL_KEY =
            ContextLocal.registerLocal(DomainEventPublisher.class);

    private boolean publishing;
    private List subscribers;

    public static DomainEventPublisher instance() {
        Context context = Vertx.currentContext();
        DomainEventPublisher domainEventPublisher =
                context.getLocal(CONTEXT_LOCAL_KEY, AccessMode.CONCURRENT, () -> new DomainEventPublisher());
        return domainEventPublisher;
    }

    public <T> void publish(final T aDomainEvent) {
        if (!this.isPublishing() && this.hasSubscribers()) {

            try {
                this.setPublishing(true);

                Class<?> eventType = aDomainEvent.getClass();

                List<DomainEventSubscriber<T>> allSubscribers = this.subscribers();

                for (DomainEventSubscriber<T> subscriber : allSubscribers) {
                    Class<?> subscribedToType = subscriber.subscribedToEventType();

                    if (eventType == subscribedToType || subscribedToType == DomainEvent.class) {
                        subscriber.handleEvent(aDomainEvent);
                    }
                }

            } finally {
                this.setPublishing(false);
            }
        }
    }

    public void publishAll(Collection<DomainEvent> aDomainEvents) {
        for (DomainEvent domainEvent : aDomainEvents) {
            this.publish(domainEvent);
        }
    }

    public void reset() {
        if (!this.isPublishing()) {
            this.setSubscribers(null);
        }
    }

    public <T> void subscribe(DomainEventSubscriber<T> aSubscriber) {
        if (!this.isPublishing()) {
            this.ensureSubscribersList();

            this.subscribers().add(aSubscriber);
        }
    }

    private DomainEventPublisher() {
        super();

        this.setPublishing(false);
        this.ensureSubscribersList();
    }

    private void ensureSubscribersList() {
        if (!this.hasSubscribers()) {
            this.setSubscribers(new ArrayList());
        }
    }

    private boolean isPublishing() {
        return this.publishing;
    }

    private void setPublishing(boolean aFlag) {
        this.publishing = aFlag;
    }

    private boolean hasSubscribers() {
        return this.subscribers() != null;
    }

    private List subscribers() {
        return this.subscribers;
    }

    private void setSubscribers(List aSubscriberList) {
        this.subscribers = aSubscriberList;
    }
}

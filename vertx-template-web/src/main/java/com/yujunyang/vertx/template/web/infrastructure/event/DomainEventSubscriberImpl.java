/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import com.yujunyang.vertx.template.common.event.DomainEventPublisher;
import com.yujunyang.vertx.template.common.event.DomainEventSubscriber;
import com.yujunyang.vertx.template.common.event.EventStore;
import io.vertx.core.Future;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DomainEventSubscriberImpl implements DomainEventSubscriber {
    private EventStore eventStore;

    @Inject
    public DomainEventSubscriberImpl(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    @Override
    public Future<Void> handleEvents() {
        List<DomainEvent> events = DomainEventPublisher.getEvents();
        if (events.isEmpty()) {
            return Future.succeededFuture();
        }

        List<Future<Void>> futures = new ArrayList<>();
        for (DomainEvent event : events) {
            futures.add(eventStore.add(event, null));
        }
        return Future.all(futures).mapEmpty();
    }
}

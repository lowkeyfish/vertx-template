/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.core.spi.context.storage.AccessMode;
import io.vertx.core.spi.context.storage.ContextLocal;
import java.util.Collection;
import java.util.List;

public final class DomainEventPublisher {
    private static final ContextLocal<EventList> CONTEXT_LOCAL_KEY_EVENTS = ContextLocal.registerLocal(EventList.class);

    public static void publish(DomainEvent domainEvent) {
        eventList().add(domainEvent);
    }

    public static void publishAll(Collection<DomainEvent> domainEvents) {
        eventList().addAll(domainEvents);
    }

    private static EventList eventList() {
        Context context = Vertx.currentContext();
        EventList eventList = context.getLocal(CONTEXT_LOCAL_KEY_EVENTS, AccessMode.CONCURRENT, EventList::new);
        return eventList;
    }

    public static List<DomainEvent> getEvents() {
        return eventList().getAll();
    }

    public static void clear() {
        eventList().clear();
    }

    public static void reset() {
        Context context = Vertx.currentContext();
        context.removeLocal(CONTEXT_LOCAL_KEY_EVENTS);
    }
}

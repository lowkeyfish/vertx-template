/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event;

import io.vertx.core.internal.VertxBootstrap;
import io.vertx.core.spi.VertxServiceProvider;
import io.vertx.core.spi.context.storage.ContextLocal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * CONTEXT_LOCAL_KEY_EVENTS放到EventList中，EventList作为SPI服务加载 如果CONTEXT_LOCAL_KEY_EVENTS仅作为一个普通的类的静态变量EventList eventList =
 * context.getLocal(EventList.CONTEXT_LOCAL_KEY_EVENTS, AccessMode.CONCURRENT, EventList::new);使用时将报错
 */
public class EventList implements VertxServiceProvider {
    static final ContextLocal<EventList> CONTEXT_LOCAL_KEY_EVENTS = ContextLocal.registerLocal(EventList.class);

    private final List<DomainEvent> events = new ArrayList<>();

    public void add(DomainEvent domainEvent) {
        events.add(domainEvent);
    }

    public void addAll(Collection<DomainEvent> domainEvents) {
        events.addAll(domainEvents);
    }

    public List<DomainEvent> getAll() {
        return List.copyOf(events);
    }

    public void clear() {
        events.clear();
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    @Override
    public void init(VertxBootstrap vertxBootstrap) {}
}

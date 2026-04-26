/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EventList {
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
}

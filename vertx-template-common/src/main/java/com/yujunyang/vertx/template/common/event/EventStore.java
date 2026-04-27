/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event;

import io.vertx.core.Future;
import java.util.List;

public interface EventStore {

    Future<Void> add(DomainEvent domainEvent);

    <T extends DomainEvent> Future<List<StoredEvent>> storedEventsSince(
            Class<T> domainEventClass, long eventId, int limit);
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event.persistence.repository;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import com.yujunyang.vertx.template.common.event.EventStore;
import com.yujunyang.vertx.template.common.event.StoredEvent;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.EventStoreDataAccessor;
import io.vertx.core.Future;
import io.vertx.sqlclient.SqlConnection;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventStoreImpl implements EventStore {
    private EventStoreDataAccessor eventStoreDataAccessor;

    @Inject
    public EventStoreImpl(EventStoreDataAccessor eventStoreDataAccessor) {
        this.eventStoreDataAccessor = eventStoreDataAccessor;
    }

    @Override
    public Future<Void> add(DomainEvent domainEvent, SqlConnection sqlConnection) {
        return null;
    }

    @Override
    public <T extends DomainEvent> Future<List<StoredEvent>> storedEventsSince(
            Class<T> domainEventClass, long eventId, int limit, SqlConnection sqlConnection) {
        return null;
    }
}

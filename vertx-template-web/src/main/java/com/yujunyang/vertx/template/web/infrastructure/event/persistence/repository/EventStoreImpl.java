/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event.persistence.repository;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import com.yujunyang.vertx.template.common.event.EventStore;
import com.yujunyang.vertx.template.common.event.StoredEvent;
import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.EventStoreDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.model.EventStoreDataModel;
import io.vertx.core.Future;
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
    public Future<Void> add(DomainEvent domainEvent) {
        return eventStoreDataAccessor.insert(convert(domainEvent)).mapEmpty();
    }

    @Override
    public <T extends DomainEvent> Future<List<StoredEvent>> storedEventsSince(
            Class<T> domainEventClass, long eventId, int limit) {
        return eventStoreDataAccessor
                .select(domainEventClass.getName(), eventId, limit)
                .map(this::convert);
    }

    private EventStoreDataModel convert(DomainEvent domainEvent) {
        return new EventStoreDataModel(
                domainEvent.storedEventType(),
                JacksonUtils.serialize(domainEvent, JacksonUtils.DEFAULT_OBJECT_MAPPER),
                domainEvent.getTimestamp(),
                domainEvent.storedEventKey());
    }

    private StoredEvent convert(EventStoreDataModel dataModel) {
        return new StoredEvent(
                dataModel.id(),
                dataModel.eventBody(),
                dataModel.timestamp(),
                dataModel.eventType(),
                dataModel.eventKey());
    }

    private List<StoredEvent> convert(List<EventStoreDataModel> dataModels) {
        return dataModels.stream().map(this::convert).toList();
    }
}

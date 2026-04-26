/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event.di;

import com.yujunyang.vertx.template.common.event.DomainEventSubscriber;
import com.yujunyang.vertx.template.common.event.EventStore;
import com.yujunyang.vertx.template.common.event.notification.PublishedNotificationTrackerStore;
import com.yujunyang.vertx.template.web.infrastructure.event.DomainEventSubscriberImpl;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.EventStoreDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.PublishedNotificationTrackerDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.impl.EventStoreDataAccessorImpl;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.impl.PublishedNotificationTrackerDataAccessorImpl;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.repository.EventStoreImpl;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.repository.PublishedNotificationTrackerStoreImpl;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class EventModule {
    @Binds
    abstract EventStoreDataAccessor bindEventStoreDataAccessor(EventStoreDataAccessorImpl eventStoreDataAccessor);

    @Binds
    abstract PublishedNotificationTrackerDataAccessor bindPublishedNotificationTrackerDataAccessor(
            PublishedNotificationTrackerDataAccessorImpl publishedNotificationTrackerDataAccessor);

    @Binds
    abstract EventStore bindEventStore(EventStoreImpl eventStore);

    @Binds
    abstract PublishedNotificationTrackerStore bindPublishedNotificationTrackerStore(
            PublishedNotificationTrackerStoreImpl publishedNotificationTrackerStore);

    @Binds
    abstract DomainEventSubscriber provideDomainEventSubscriber(DomainEventSubscriberImpl domainEventSubscriber);
}

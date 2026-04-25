package com.yujunyang.vertx.template.web.infrastructure.event.persistence.repository;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import com.yujunyang.vertx.template.common.event.notification.DomainEventNotification;
import com.yujunyang.vertx.template.common.event.notification.PublishedNotificationTracker;
import com.yujunyang.vertx.template.common.event.notification.PublishedNotificationTrackerStore;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.PublishedNotificationTrackerDataAccessor;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PublishedNotificationTrackerStoreImpl implements PublishedNotificationTrackerStore {
    private PublishedNotificationTrackerDataAccessor publishedNotificationTrackerDataAccessor;

    @Inject
    public PublishedNotificationTrackerStoreImpl(
            PublishedNotificationTrackerDataAccessor publishedNotificationTrackerDataAccessor) {
        this.publishedNotificationTrackerDataAccessor = publishedNotificationTrackerDataAccessor;
    }

    @Override
    public <T extends DomainEvent> PublishedNotificationTracker publishedNotificationTracker(
            Class<T> domainEventClass) {
        return null;
    }

    @Override
    public void trackMostRecentPublishedNotification(
            PublishedNotificationTracker publishedNotificationTracker, List<DomainEventNotification> notifications) {}
}

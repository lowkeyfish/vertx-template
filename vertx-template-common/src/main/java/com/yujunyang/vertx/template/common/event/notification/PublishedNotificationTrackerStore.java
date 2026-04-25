package com.yujunyang.vertx.template.common.event.notification;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import java.util.List;

public interface PublishedNotificationTrackerStore {

    <T extends DomainEvent> PublishedNotificationTracker publishedNotificationTracker(Class<T> domainEventClass);

    void trackMostRecentPublishedNotification(
            PublishedNotificationTracker publishedNotificationTracker, List<DomainEventNotification> notifications);
}

package com.yujunyang.vertx.template.common.event.notification;

import com.yujunyang.vertx.template.common.event.DomainEvent;
import io.vertx.core.Future;

public interface DomainEventNotificationService {
    <T extends DomainEvent> Future<Void> publishNotifications(Class<T> domainEventClass, int limit);
}

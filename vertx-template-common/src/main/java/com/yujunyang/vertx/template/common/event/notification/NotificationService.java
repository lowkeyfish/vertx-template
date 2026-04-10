/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event.notification;

import com.yujunyang.vertx.template.common.event.DomainEvent;

public class NotificationService {
    private NotificationPublisher notificationPublisher;

    public NotificationService(NotificationPublisher notificationPublisher) {
        this.notificationPublisher = notificationPublisher;
    }

    public <T extends DomainEvent> void publishNotifications(Class<T> domainEventClass, int limit) {
        // String lockKey = MessageFormat.format("NotificationService_{0}", domainEventClass.getName());
        // RLock lock = redissonClient.getLock(lockKey);
        // try {
        //    if (lock.tryLock()) {
        //        while (notificationPublisher.publishNotifications(domainEventClass, limit)) {
        //        }
        //    } else {
        //        LOGGER.trace("事件通知, DomainEvent({}) 获取锁失败.", domainEventClass.getName());
        //    }
        // } catch (Exception e) {
        //    LOGGER.error("事件通知, DomainEvent({}) 出错", domainEventClass.getName(), e);
        // } finally {
        //    if (lock.isHeldByCurrentThread()) {
        //        lock.unlock();
        //    }
        // }
    }
}

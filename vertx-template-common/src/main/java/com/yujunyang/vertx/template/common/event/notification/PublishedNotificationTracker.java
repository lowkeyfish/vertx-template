/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.event.notification;

public class PublishedNotificationTracker {
    private long publishedNotificationTrackerId;
    private long mostRecentPublishedEventId;
    private String eventType;
    private long concurrencyVersion;

    public PublishedNotificationTracker(
            long publishedNotificationTrackerId,
            long mostRecentPublishedEventId,
            String eventType,
            long concurrencyVersion) {
        this.publishedNotificationTrackerId = publishedNotificationTrackerId;
        this.mostRecentPublishedEventId = mostRecentPublishedEventId;
        this.eventType = eventType;
        this.concurrencyVersion = concurrencyVersion;
    }

    public PublishedNotificationTracker(String eventType, long concurrencyVersion) {
        this.eventType = eventType;
        this.concurrencyVersion = concurrencyVersion;
    }

    public long getMostRecentPublishedEventId() {
        return mostRecentPublishedEventId;
    }

    public String getEventType() {
        return eventType;
    }

    public long getConcurrencyVersion() {
        return concurrencyVersion;
    }

    public long getPublishedNotificationTrackerId() {
        return publishedNotificationTrackerId;
    }

    public void setConcurrencyVersion(long concurrencyVersion) {
        this.concurrencyVersion = concurrencyVersion;
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.authentication.session;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yujunyang.vertx.template.common.event.DomainEvent;

public class SessionRevoked implements DomainEvent {
    private long timestamp;
    private String id;

    @JsonCreator
    public SessionRevoked(@JsonProperty("id") String id, @JsonProperty("timestamp") long timestamp) {
        this.id = id;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String notificationRoutingKey() {
        return "Session." + notificationType();
    }

    @Override
    public String storedEventKey() {
        return String.valueOf(id);
    }
}

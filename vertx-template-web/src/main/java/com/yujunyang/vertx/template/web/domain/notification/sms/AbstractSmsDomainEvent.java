/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yujunyang.vertx.template.common.event.DomainEvent;

public abstract class AbstractSmsDomainEvent implements DomainEvent {
    private long timestamp;
    private long smsId;

    @JsonCreator
    public AbstractSmsDomainEvent(@JsonProperty("timestamp") long timestamp, @JsonProperty("smsId") long smsId) {
        this.timestamp = timestamp;
        this.smsId = smsId;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    public long getSmsId() {
        return smsId;
    }

    @Override
    public String storedEventKey() {
        return String.valueOf(smsId);
    }

    @Override
    public String notificationRoutingKey() {
        return "Sms." + notificationType();
    }
}

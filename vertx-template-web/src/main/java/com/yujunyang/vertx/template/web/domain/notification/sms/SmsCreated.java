/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SmsCreated extends AbstractSmsDomainEvent {
    @JsonCreator
    public SmsCreated(@JsonProperty("timestamp") long timestamp, @JsonProperty("smsId") long smsId) {
        super(timestamp, smsId);
    }
}

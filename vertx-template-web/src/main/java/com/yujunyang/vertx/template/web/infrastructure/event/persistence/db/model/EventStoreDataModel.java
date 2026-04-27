/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.model;

public record EventStoreDataModel(long id, String eventType, String eventBody, long timestamp, String eventKey) {

    public EventStoreDataModel(String eventType, String eventBody, long timestamp, String eventKey) {
        this(0, eventType, eventBody, timestamp, eventKey);
    }
}

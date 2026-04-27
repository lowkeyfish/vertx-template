/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event.persistence.db;

import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.model.EventStoreDataModel;
import io.vertx.core.Future;
import java.util.List;

public interface EventStoreDataAccessor {
    Future<Integer> insert(EventStoreDataModel dataModel);

    Future<List<EventStoreDataModel>> select(String eventType, long minEventId, int limit);
}

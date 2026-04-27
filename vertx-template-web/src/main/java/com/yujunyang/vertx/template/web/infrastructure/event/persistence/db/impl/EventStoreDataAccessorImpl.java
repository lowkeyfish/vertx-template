/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.impl;

import com.yujunyang.vertx.template.common.db.connection.SqlConnectionTemplate;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.EventStoreDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.event.persistence.db.model.EventStoreDataModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EventStoreDataAccessorImpl implements EventStoreDataAccessor {
    private static final String SQL_INSERT = """
        INSERT INTO `event_store`(
            `event_type`,
            `event_body`,
            `timestamp`,
            `event_key`
        ) VALUES(
            ?,
            ?,
            ?,
            ?
        )
        """;
    private static final String SQL_SELECT_BY_EVENT_TYPE_AND_MIN_ID = """
        SELECT
            `id`,
            `event_typ`,
            `event_body`,
            `timestamp`,
            `event_key`
        FROM
            `event_store`
        WHERE
            `id` > ? AND `event_type` = ? AND `deleted` = 0
        ORDER BY `id` ASC
        LIMIT ?
        """;

    private SqlConnectionTemplate sqlConnectionTemplate;

    @Inject
    public EventStoreDataAccessorImpl(SqlConnectionTemplate sqlConnectionTemplate) {
        this.sqlConnectionTemplate = sqlConnectionTemplate;
    }

    @Override
    public Future<Integer> insert(EventStoreDataModel dataModel) {
        Tuple tuple =
                Tuple.of(dataModel.eventType(), dataModel.eventBody(), dataModel.timestamp(), dataModel.eventKey());

        return sqlConnectionTemplate.withConnection(sqlConnection ->
                sqlConnection.preparedQuery(SQL_INSERT).execute(tuple).map(rows -> rows.rowCount()));
    }

    @Override
    public Future<List<EventStoreDataModel>> select(String eventType, long minEventId, int limit) {
        Tuple tuple = Tuple.of(minEventId, eventType, limit);
        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(SQL_SELECT_BY_EVENT_TYPE_AND_MIN_ID)
                .execute(tuple)
                .map(rows -> convert(rows)));
    }

    private EventStoreDataModel convert(Row row) {
        return new EventStoreDataModel(
                row.getLong("id"),
                row.getString("event_type"),
                row.getString("event_body"),
                row.getLong("timestamp"),
                row.getString("event_key"));
    }

    private List<EventStoreDataModel> convert(RowSet<Row> rows) {
        List<EventStoreDataModel> ret = new ArrayList<>();
        rows.forEach(row -> ret.add(convert(row)));
        return ret;
    }
}

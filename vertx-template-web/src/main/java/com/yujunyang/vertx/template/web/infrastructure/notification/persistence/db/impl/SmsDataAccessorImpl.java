/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.impl;

import com.yujunyang.vertx.template.common.db.connection.SqlConnectionTemplate;
import com.yujunyang.vertx.template.common.db.sql.SelectCondition;
import com.yujunyang.vertx.template.common.db.sql.SelectCondition.SqlBuildResult;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.SmsDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.model.SmsDataModel;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SmsDataAccessorImpl implements SmsDataAccessor {
    private static final String SQL_INSERT = """
        INSERT INTO `sms`(
            `id`,
            `mobile`,
            `template_parameters`,
            `status`,
            `create_time`,
            `notification_category`,
            `notification_category_metadata`
        ) VALUES(
            #{id},
            #{mobile},
            #{templateParameters},
            #{status},
            #{createTime},
            #{notificationCategory},
            #{notificationCategoryMetadata}
        );
        """;
    private static final String SQL_UPDATE = """
        UPDATE `sms`
        SET `status` = #{status},
            `send_time` = #{sendTime},
            `notification_vendor` = #{notificationVendor},
            `send_details` = #{sendDetails},
            `send_response` = #{sendResponse}
        WHERE `id` = #{id};
        """;
    private static final String SQL_ALL_COLUMNS = """
        `id`,
        `mobile`,
        `template_parameters`,
        `status`,
        `create_time`,
        `update_time`,
        `deleted`,
        `notification_category`,
        `notification_vendor`,
        `send_time`,
        `send_details`,
        `notification_category_metadata`,
        `send_response`
        """;
    private static final String SQL_SELECT_BASE_PATTERN = """
        SELECT
            {0}
        FROM
            `sms`
        """;
    private static final String SQL_SELECT_BY_ID_PATTERN = """
        SELECT
            {0}
        FROM
            `sms`
        WHERE
            `id` = ?
        """;
    private static final String SQL_COUNT_BASE = """
        SELECT COUNT(1) AS `count` FROM `sms`
        """;

    private SqlConnectionTemplate sqlConnectionTemplate;

    @Inject
    public SmsDataAccessorImpl(SqlConnectionTemplate sqlConnectionTemplate) {
        this.sqlConnectionTemplate = sqlConnectionTemplate;
    }

    @Override
    public Future<Integer> insert(SmsDataModel dataModel) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", dataModel.id());
        parameters.put("mobile", dataModel.mobile());
        parameters.put("templateParameters", dataModel.templateParameters());
        parameters.put("notificationCategory", dataModel.notificationCategory());
        parameters.put("notificationCategoryMetadata", dataModel.notificationCategoryMetadata());
        parameters.put("createTime", dataModel.createTime());
        parameters.put("status", dataModel.status());
        return sqlConnectionTemplate.withConnection(sqlConnection -> SqlTemplate.forQuery(sqlConnection, SQL_INSERT)
                .execute(parameters)
                .map(r -> r.rowCount()));
    }

    @Override
    public Future<Integer> update(SmsDataModel dataModel) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", dataModel.id());
        parameters.put("mobile", dataModel.mobile());
        parameters.put("templateParameters", dataModel.templateParameters());
        parameters.put("notificationCategory", dataModel.notificationCategory());
        parameters.put("notificationCategoryMetadata", dataModel.notificationCategoryMetadata());
        parameters.put("createTime", dataModel.createTime());
        parameters.put("status", dataModel.status());
        parameters.put("sendTime", dataModel.sendTime());
        parameters.put("sendDetails", dataModel.sendDetails());
        parameters.put("sendResponse", dataModel.sendResponse());
        parameters.put("notificationVendor", dataModel.notificationVendor());
        return sqlConnectionTemplate.withConnection(sqlConnection -> SqlTemplate.forQuery(sqlConnection, SQL_UPDATE)
                .execute(parameters)
                .map(r -> r.rowCount()));
    }

    @Override
    public Future<List<SmsDataModel>> select(SelectCondition selectCondition) {
        SqlBuildResult sqlBuildResult =
                selectCondition.toSql(MessageFormat.format(SQL_SELECT_BASE_PATTERN, SQL_ALL_COLUMNS));
        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(sqlBuildResult.sql())
                .execute(sqlBuildResult.tuple())
                .map(this::convert));
    }

    @Override
    public Future<Optional<SmsDataModel>> selectById(long id) {
        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(MessageFormat.format(SQL_SELECT_BY_ID_PATTERN, SQL_ALL_COLUMNS))
                .execute(Tuple.of(id))
                .map(rows -> convert(rows).stream().findFirst()));
    }

    @Override
    public Future<Integer> count(SelectCondition selectCondition) {
        SqlBuildResult sqlBuildResult = selectCondition.toSql(SQL_COUNT_BASE);
        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(sqlBuildResult.sql())
                .execute(sqlBuildResult.tuple())
                .map(rows -> rows.stream()
                        .map(n -> n.getInteger("count"))
                        .findFirst()
                        .orElse(0)));
    }

    private SmsDataModel convert(Row row) {
        return new SmsDataModel(
                row.getLong("id"),
                row.getString("mobile"),
                row.getString("templateParameters"),
                row.getInteger("notificationCategory"),
                row.getString("notificationCategoryMetadata"),
                row.getLocalDateTime("createTime"),
                row.getInteger("status"),
                row.getLocalDateTime("sendTime"),
                row.getInteger("notificationVendor"),
                row.getString("sendDetails"),
                row.getString("sendResponse"));
    }

    private List<SmsDataModel> convert(RowSet<Row> rows) {
        return rows.stream().map(this::convert).toList();
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.common.persistence;

import com.yujunyang.vertx.template.common.db.sql.SelectCondition;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import java.util.List;
import java.util.function.Function;

public interface DataAccessor {
    <T extends DataModel> Future<Integer> insertIgnore(T dataModel);

    <T extends DataModel> Future<Integer> insert(T dataModel);

    <T extends DataModel> Future<Integer> insertIgnoreBatch(List<T> dataModels);

    <T extends DataModel> Future<Integer> update(T dataModel);

    Future<Integer> delete(SelectCondition selectCondition);

    Future<Integer> logicDelete(SelectCondition selectCondition);

    <T extends DataModel> Future<List<T>> select(
            SelectCondition selectCondition, Function<RowSet<Row>, List<T>> converter);

    Future<Integer> count(SelectCondition selectCondition);
}

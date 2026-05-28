/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.common.persistence;

import com.yujunyang.vertx.template.common.data.Tuple2;
import com.yujunyang.vertx.template.common.db.connection.SqlConnectionTemplate;
import com.yujunyang.vertx.template.common.db.sql.SelectCondition;
import com.yujunyang.vertx.template.common.db.sql.SelectCondition.SqlBuildResult;
import io.jsonwebtoken.lang.Collections;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.SqlResult;
import io.vertx.sqlclient.templates.SqlTemplate;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataAccessorImpl implements DataAccessor {
    private static final String SQL_INSERT_PATTERN = """
        INSERT INTO `{0}` ({1}) VALUES ({2})
        """;
    private static final String SQL_INSERT_IGNORE_PATTERN = """
        INSERT IGNORE INTO `{0}` ({1}) VALUES ({2})
        """;
    private static final String SQL_INSERT_IGNORE_BATCH_PATTERN = """
        INSERT IGNORE INTO `{0}` ({1}) VALUES {2}
        """;
    private static final String SQL_UPDATE_PATTERN = """
        UPDATE `{0}` SET {1} WHERE {2}
        """;

    private SqlConnectionTemplate sqlConnectionTemplate;

    @Inject
    public DataAccessorImpl(SqlConnectionTemplate sqlConnectionTemplate) {
        this.sqlConnectionTemplate = sqlConnectionTemplate;
    }

    private String insertColumnsSql(DataModel dataModel) {
        return String.join(
                ", ",
                dataModel.insertValuesParameters().keySet().stream()
                        .map(n -> "`" + n + "`")
                        .toList());
    }

    private String insertValuesSql(DataModel dataModel) {
        return String.join(
                ", ",
                dataModel.insertValuesParameters().keySet().stream()
                        .map(n -> "#{" + n + "}")
                        .toList());
    }

    private Tuple2<String, Map<String, Object>> insertSqlAndParameters(DataModel dataModel) {
        return new Tuple2<>(
                MessageFormat.format(
                        SQL_INSERT_PATTERN,
                        dataModel.tableName(),
                        insertColumnsSql(dataModel),
                        insertValuesSql(dataModel)),
                dataModel.insertValuesParameters());
    }

    private Tuple2<String, Map<String, Object>> insertIgnoreSqlAndParameters(DataModel dataModel) {
        return new Tuple2<>(
                MessageFormat.format(
                        SQL_INSERT_IGNORE_PATTERN,
                        dataModel.tableName(),
                        insertColumnsSql(dataModel),
                        insertValuesSql(dataModel)),
                dataModel.insertValuesParameters());
    }

    private Tuple2<String, Map<String, Object>> insertIgnoreBatchSqlAndParameters(List<DataModel> dataModels) {
        List<String> valuesSqlList = new ArrayList<>();
        Map<String, Object> parameters = new HashMap<>();
        for (int i = 0; i < dataModels.size(); i++) {
            int index = i;
            DataModel dataModel = dataModels.get(index);
            Map<String, Object> parametersMap = dataModel.insertValuesParameters();
            String valuesSql = "("
                    + String.join(
                            ", ",
                            parametersMap.keySet().stream()
                                    .map(n -> "#{" + n + "_" + index + "}")
                                    .toList()) + ")";
            valuesSqlList.add(valuesSql);
            parametersMap.keySet().forEach(n -> parameters.put(n + "_" + index, parametersMap.get(n)));
        }

        DataModel dataModel = dataModels.get(0);
        String tableName = dataModel.tableName();
        String insertColumnsSql = insertColumnsSql(dataModel);
        String allInsertValuesSql = String.join(", ", valuesSqlList);
        return new Tuple2<>(
                MessageFormat.format(SQL_INSERT_IGNORE_BATCH_PATTERN, tableName, insertColumnsSql, allInsertValuesSql),
                parameters);
    }

    private Tuple2<String, Map<String, Object>> updateSqlAndParameters(DataModel dataModel) {
        Map<String, Object> updateSetParameters = new HashMap<>(dataModel.updateSetParameters());
        // 先清理掉version字段，防止后续融合参数时和where的参数值冲突
        updateSetParameters.remove("version");

        boolean existsVersionColumn = dataModel.columns().contains("version");
        Set<String> updateSetColumns = new HashSet<>(updateSetParameters.keySet());
        if (existsVersionColumn) {
            updateSetColumns.add("version");
        }

        String setSql = String.join(
                ", ",
                updateSetColumns.stream()
                        .map(n -> {
                            if (n.equalsIgnoreCase("version")) {
                                return "`version` = `version` + 1";
                            } else {
                                return "`" + n + "` = #{" + n + "}";
                            }
                        })
                        .toList());
        String whereSql = String.join(
                " AND ",
                dataModel.updateWhereParameters().keySet().stream()
                        .map(n -> "`" + n + "` = #{" + n + "}")
                        .toList());
        Map<String, Object> parameters = new HashMap<>(updateSetParameters);
        parameters.putAll(dataModel.updateWhereParameters());
        return new Tuple2<>(
                MessageFormat.format(SQL_UPDATE_PATTERN, dataModel.tableName(), setSql, whereSql), parameters);
    }

    @Override
    public <T extends DataModel> Future<Integer> insertIgnore(T dataModel) {
        Tuple2<String, Map<String, Object>> tuple = insertIgnoreSqlAndParameters(dataModel);
        return sqlConnectionTemplate.withConnection(sqlConnection -> SqlTemplate.forUpdate(sqlConnection, tuple.getT1())
                .execute(tuple.getT2())
                .map(SqlResult::rowCount));
    }

    @Override
    public <T extends DataModel> Future<Integer> insert(T dataModel) {
        Tuple2<String, Map<String, Object>> tuple = insertSqlAndParameters(dataModel);
        return sqlConnectionTemplate.withConnection(sqlConnection -> SqlTemplate.forUpdate(sqlConnection, tuple.getT1())
                .execute(tuple.getT2())
                .map(SqlResult::rowCount));
    }

    @Override
    public <T extends DataModel> Future<Integer> insertIgnoreBatch(List<T> dataModels) {
        if (Collections.isEmpty(dataModels)) {
            return Future.succeededFuture(0);
        }

        Tuple2<String, Map<String, Object>> tuple = insertIgnoreBatchSqlAndParameters((List<DataModel>) dataModels);
        return sqlConnectionTemplate.withConnection(sqlConnection -> SqlTemplate.forUpdate(sqlConnection, tuple.getT1())
                .execute(tuple.getT2())
                .map(SqlResult::rowCount));
    }

    @Override
    public <T extends DataModel> Future<Integer> update(T dataModel) {
        Tuple2<String, Map<String, Object>> tuple = updateSqlAndParameters(dataModel);
        return sqlConnectionTemplate.withConnection(sqlConnection -> SqlTemplate.forUpdate(sqlConnection, tuple.getT1())
                .execute(tuple.getT2())
                .map(SqlResult::rowCount));
    }

    @Override
    public Future<Integer> delete(SelectCondition selectCondition) {
        SqlBuildResult sqlBuildResult = selectCondition.toDeleteSql();
        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(sqlBuildResult.sql())
                .execute(sqlBuildResult.tuple())
                .map(SqlResult::rowCount));
    }

    @Override
    public Future<Integer> logicDelete(SelectCondition selectCondition) {
        SqlBuildResult sqlBuildResult = selectCondition.toLogicDeleteSql();
        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(sqlBuildResult.sql())
                .execute(sqlBuildResult.tuple())
                .map(SqlResult::rowCount));
    }

    @Override
    public <T extends DataModel> Future<List<T>> select(
            SelectCondition selectCondition, Function<RowSet<Row>, List<T>> converter) {
        SqlBuildResult sqlBuildResult = selectCondition.toSelectSql();

        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(sqlBuildResult.sql())
                .execute(sqlBuildResult.tuple())
                .map(rows -> converter.apply(rows)));
    }

    @Override
    public Future<Integer> count(SelectCondition selectCondition) {
        SqlBuildResult sqlBuildResult = selectCondition.toCountSql();
        return sqlConnectionTemplate.withConnection(sqlConnection -> sqlConnection
                .preparedQuery(sqlBuildResult.sql())
                .execute(sqlBuildResult.tuple())
                .map(rows -> rows.stream()
                        .map(n -> n.getInteger("count"))
                        .findFirst()
                        .orElse(0)));
    }
}

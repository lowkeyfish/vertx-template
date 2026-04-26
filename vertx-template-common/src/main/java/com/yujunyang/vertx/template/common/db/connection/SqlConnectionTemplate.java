/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.db.connection;

import com.yujunyang.vertx.template.common.db.transaction.TransactionContext;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Tuple;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SqlConnectionTemplate {
    private Pool pool;

    @Inject
    public SqlConnectionTemplate(Pool pool) {
        this.pool = pool;
    }

    public <T> Future<T> withConnection(Function<SqlConnection, Future<T>> action) {
        SqlConnection sqlConnection = TransactionContext.getCurrentConnection();
        if (sqlConnection != null) {
            // 在事务中，直接使用事务连接
            return action.apply(sqlConnection);
        } else {
            // 无事务，获取临时连接，操作后关闭
            return pool.getConnection().compose(conn -> action.apply(conn).eventually(conn::close));
        }
    }

    public Future<Void> executeUpdate(String sql, Tuple params) {
        return withConnection(conn -> conn.preparedQuery(sql).execute(params).mapEmpty());
    }
}

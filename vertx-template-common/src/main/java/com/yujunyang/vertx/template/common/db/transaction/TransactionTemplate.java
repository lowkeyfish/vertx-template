package com.yujunyang.vertx.template.common.db.transaction;

import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransactionTemplate {
    private final Pool pool;

    @Inject
    public TransactionTemplate(Pool pool) {
        this.pool = pool;
    }

    public <T> Future<T> withTransaction(Function<SqlConnection, Future<T>> action) {
        if (TransactionContext.getCurrentConnection() != null) {
            return action.apply(TransactionContext.getCurrentConnection());
        }

        // 没有外层事务：开启新事务
        return pool.getConnection().compose(conn -> conn.begin().compose(tx -> {
            TransactionContext.bindTransaction(conn, tx);
            return action.apply(conn).onComplete(ar -> {
                if (ar.succeeded()) {
                    tx.commit().onComplete(x -> {
                        TransactionContext.unbind();
                        conn.close();
                    });
                } else {
                    tx.rollback().onComplete(x -> {
                        TransactionContext.unbind();
                        conn.close();
                    });
                }
            });
        }));
    }
}

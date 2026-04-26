/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.db.transaction;

import com.yujunyang.vertx.template.common.event.DomainEventSubscriber;
import io.vertx.core.Future;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.SqlConnection;
import java.util.function.Function;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TransactionTemplate {
    private final Pool pool;
    private DomainEventSubscriber domainEventSubscriber;

    @Inject
    public TransactionTemplate(Pool pool, DomainEventSubscriber domainEventSubscriber) {
        this.pool = pool;
        this.domainEventSubscriber = domainEventSubscriber;
    }

    public <T> Future<T> withTransaction(Function<SqlConnection, Future<T>> action) {
        if (TransactionContext.getCurrentConnection() != null) {
            return action.apply(TransactionContext.getCurrentConnection());
        }

        // 没有外层事务：开启新事务
        return pool.getConnection().compose(conn -> conn.begin().compose(tx -> {
            TransactionContext.bindTransaction(conn, tx);

            return action.apply(conn)
                    .compose(result -> domainEventSubscriber.handleEvents().map(result))
                    .compose(result -> tx.commit().map(result))
                    .recover(throwable -> tx.rollback()
                            .onComplete(unused -> {
                                TransactionContext.unbind();
                                conn.close();
                            })
                            .transform(ignored -> Future.failedFuture(throwable)))
                    .compose(result -> {
                        TransactionContext.unbind();
                        return conn.close().map(result);
                    });
        }));
    }
}

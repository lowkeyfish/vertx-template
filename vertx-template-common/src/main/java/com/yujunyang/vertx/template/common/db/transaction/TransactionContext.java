/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.db.transaction;

import io.vertx.core.Context;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.SqlConnection;
import io.vertx.sqlclient.Transaction;

public final class TransactionContext {
    private static final String KEY_CONN = "__TX_CONN";
    private static final String KEY_TX = "__TX_TX";

    private TransactionContext() {}

    public static SqlConnection getCurrentConnection() {
        Context ctx = Vertx.currentContext();
        return ctx != null ? (SqlConnection) ctx.get(KEY_CONN) : null;
    }

    public static Transaction getCurrentTransaction() {
        Context ctx = Vertx.currentContext();
        return ctx != null ? (Transaction) ctx.get(KEY_TX) : null;
    }

    static void bindTransaction(SqlConnection conn, Transaction tx) {
        Context ctx = Vertx.currentContext();
        ctx.put(KEY_CONN, conn);
        ctx.put(KEY_TX, tx);
    }

    static void unbind() {
        Context ctx = Vertx.currentContext();
        ctx.remove(KEY_CONN);
        ctx.remove(KEY_TX);
    }
}

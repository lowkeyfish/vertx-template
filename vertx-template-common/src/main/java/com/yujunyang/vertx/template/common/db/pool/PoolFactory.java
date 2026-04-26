/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.db.pool;

import com.yujunyang.vertx.template.common.config.DatasourceConfig;
import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLBuilder;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;

public final class PoolFactory {

    public static Pool createMySQLPool(Vertx vertx, DatasourceConfig datasourceConfig) {
        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setPort(datasourceConfig.getPort())
                .setHost(datasourceConfig.getHost())
                .setDatabase(datasourceConfig.getDatabase())
                .setUser(datasourceConfig.getUser())
                .setPassword(datasourceConfig.getPassword());
        PoolOptions poolOptions =
                new PoolOptions().setMaxSize(5).setShared(true).setName("mysql-pool");
        Pool pool = MySQLBuilder.pool()
                .with(poolOptions)
                .connectingTo(connectOptions)
                .using(vertx)
                .build();
        return pool;
    }
}

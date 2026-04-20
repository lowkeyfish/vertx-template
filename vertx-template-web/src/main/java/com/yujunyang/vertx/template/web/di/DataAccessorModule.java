/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.web.infrastructure.persistence.db.TestDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.persistence.db.impl.TestDataAccessorImpl;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class DataAccessorModule {
    @Binds
    public abstract TestDataAccessor bingTestDataAccessor(TestDataAccessorImpl testDataAccessor);
}

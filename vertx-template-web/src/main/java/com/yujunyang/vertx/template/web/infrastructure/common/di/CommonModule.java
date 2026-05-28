/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.common.di;

import com.yujunyang.vertx.template.web.infrastructure.common.persistence.DataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.common.persistence.DataAccessorImpl;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class CommonModule {
    @Binds
    public abstract DataAccessor bindDataAccessor(DataAccessorImpl accessor);
}

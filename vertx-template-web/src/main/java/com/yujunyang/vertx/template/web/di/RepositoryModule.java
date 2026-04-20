/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.web.domain.test.TestRepository;
import com.yujunyang.vertx.template.web.infrastructure.persistence.repository.TestRepositoryImpl;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class RepositoryModule {
    @Binds
    public abstract TestRepository bingTestRepository(TestRepositoryImpl testRepository);
}

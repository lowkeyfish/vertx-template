/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.web.application.TestApplicationService;
import com.yujunyang.vertx.template.web.domain.test.TestRepository;
import com.yujunyang.vertx.template.web.infrastructure.persistence.db.TestDataAccessor;
import dagger.Component;

@Component(modules = {DataAccessorModule.class, RepositoryModule.class})
public interface AppComponent {
    TestApplicationService getTestApplicationService();

    TestRepository getTestRepository();

    TestDataAccessor getTestDataAccessor();
}

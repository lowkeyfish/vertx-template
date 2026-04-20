/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.persistence.repository;

import com.yujunyang.vertx.template.web.domain.test.Test;
import com.yujunyang.vertx.template.web.domain.test.TestId;
import com.yujunyang.vertx.template.web.domain.test.TestRepository;
import com.yujunyang.vertx.template.web.infrastructure.persistence.db.TestDataAccessor;
import javax.inject.Inject;

public class TestRepositoryImpl implements TestRepository {
    private TestDataAccessor testDataAccessor;

    @Inject
    public TestRepositoryImpl(TestDataAccessor testDataAccessor) {
        this.testDataAccessor = testDataAccessor;
    }

    @Override
    public Test findById(TestId id) {
        return new Test();
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.persistence.db.impl;

import com.yujunyang.vertx.template.web.infrastructure.persistence.db.TestDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.persistence.db.model.TestDataModel;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TestDataAccessorImpl implements TestDataAccessor {
    @Inject
    public TestDataAccessorImpl() {}

    @Override
    public TestDataModel selectById(long id) {
        return null;
    }
}

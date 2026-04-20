/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.persistence.db;

import com.yujunyang.vertx.template.web.infrastructure.persistence.db.model.TestDataModel;

public interface TestDataAccessor {
    TestDataModel selectById(long id);
}

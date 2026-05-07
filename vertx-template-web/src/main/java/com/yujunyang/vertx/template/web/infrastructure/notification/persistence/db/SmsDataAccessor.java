/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db;

import com.yujunyang.vertx.template.common.db.sql.SelectCondition;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.model.SmsDataModel;
import io.vertx.core.Future;
import java.util.List;
import java.util.Optional;

public interface SmsDataAccessor {
    Future<Integer> insert(SmsDataModel dataModel);

    Future<Integer> update(SmsDataModel dataModel);

    Future<List<SmsDataModel>> select(SelectCondition selectCondition);

    Future<Optional<SmsDataModel>> selectById(long id);

    Future<Integer> count(SelectCondition selectCondition);
}

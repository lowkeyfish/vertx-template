/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.yujunyang.vertx.template.common.domain.id.LongIdGenerator;
import io.vertx.core.Future;
import java.util.List;
import java.util.Optional;

public interface SmsRepository extends LongIdGenerator {
    default SmsId nextId() {
        return new SmsId(nextLongId());
    }

    Future<Void> add(Sms sms);

    Future<Void> save(Sms sms);

    Future<Optional<Sms>> findById(SmsId smsId);

    Future<Integer> countByCondition(SmsQueryCondition queryCondition);

    Future<List<Sms>> findByCondition(SmsQueryCondition queryCondition);
}

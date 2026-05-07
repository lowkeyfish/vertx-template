/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.notification.persistence.repository;

import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import com.yujunyang.vertx.template.web.domain.notification.NotificationCategoryType;
import com.yujunyang.vertx.template.web.domain.notification.NotificationVendorType;
import com.yujunyang.vertx.template.web.domain.notification.sms.Sms;
import com.yujunyang.vertx.template.web.domain.notification.sms.SmsId;
import com.yujunyang.vertx.template.web.domain.notification.sms.SmsQueryCondition;
import com.yujunyang.vertx.template.web.domain.notification.sms.SmsRepository;
import com.yujunyang.vertx.template.web.domain.notification.sms.SmsStatusType;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.SmsDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.model.SmsDataModel;
import io.vertx.core.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SmsRepositoryImpl implements SmsRepository {
    private SmsDataAccessor smsDataAccessor;

    @Inject
    public SmsRepositoryImpl(SmsDataAccessor smsDataAccessor) {
        this.smsDataAccessor = smsDataAccessor;
    }

    @Override
    public Future<Void> add(Sms sms) {
        return smsDataAccessor.insert(convert(sms)).mapEmpty();
    }

    @Override
    public Future<Void> save(Sms sms) {
        return smsDataAccessor.update(convert(sms)).compose(count -> {
            if (count == 1) {
                return Future.succeededFuture();
            }
            return Future.failedFuture(new SystemException("Sms数据库更新失败,影响行数非1", Map.of("updateRowCount", count)));
        });
    }

    @Override
    public Future<Optional<Sms>> findById(SmsId smsId) {
        return smsDataAccessor
                .selectById(smsId.getId())
                .compose(smsDataModelOptional -> Future.succeededFuture(smsDataModelOptional.map(this::convert)));
    }

    @Override
    public Future<Integer> countByCondition(SmsQueryCondition queryCondition) {
        return Future.succeededFuture(0);
    }

    @Override
    public Future<List<Sms>> findByCondition(SmsQueryCondition queryCondition) {
        return Future.succeededFuture(new ArrayList<>());
    }

    private SmsDataModel convert(Sms sms) {
        return new SmsDataModel(
                sms.getId().getId(),
                sms.getMobile(),
                JacksonUtils.serialize(sms.getTemplateParameters()),
                sms.getNotificationCategory().getValue(),
                sms.getNotificationCategoryMetadata(),
                sms.getCreateTime(),
                sms.getStatus().getValue(),
                sms.getSendTime(),
                Optional.ofNullable(sms.getNotificationVendor())
                        .map(n -> n.getValue())
                        .orElse(0),
                sms.getSendDetails(),
                sms.getSendResponse());
    }

    private Sms convert(SmsDataModel dataModel) {
        return new Sms(
                new SmsId(dataModel.id()),
                dataModel.mobile(),
                JacksonUtils.deserialize(dataModel.templateParameters(), Map.class),
                NotificationCategoryType.parse(dataModel.notificationCategory()),
                dataModel.notificationCategoryMetadata(),
                dataModel.sendTime(),
                dataModel.createTime(),
                NotificationVendorType.parse(dataModel.notificationVendor()),
                SmsStatusType.parse(dataModel.status()),
                dataModel.sendDetails(),
                dataModel.sendResponse());
    }
}

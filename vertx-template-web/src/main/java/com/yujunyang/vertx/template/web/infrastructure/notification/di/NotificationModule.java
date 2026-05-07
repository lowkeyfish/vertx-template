/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.notification.di;

import com.yujunyang.vertx.template.web.domain.notification.sms.SmsRepository;
import com.yujunyang.vertx.template.web.domain.notification.sms.SmsSendService;
import com.yujunyang.vertx.template.web.infrastructure.notification.AliyunSmsSendService;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.SmsDataAccessor;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.impl.SmsDataAccessorImpl;
import com.yujunyang.vertx.template.web.infrastructure.notification.persistence.repository.SmsRepositoryImpl;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class NotificationModule {
    @Binds
    public abstract SmsSendService bindSmsSendService(AliyunSmsSendService smsSendService);

    @Binds
    public abstract SmsRepository bindSmsRepository(SmsRepositoryImpl smsRepository);

    @Binds
    public abstract SmsDataAccessor bindSmdDataAccessor(SmsDataAccessorImpl smsDataAccessor);
}

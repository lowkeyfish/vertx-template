/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.yujunyang.vertx.template.web.domain.notification.NotificationCategoryType;
import io.vertx.core.Future;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SmsService {

    private SmsSendService smsSendService;
    private SmsRepository smsRepository;
    private SmsSendInterceptService smsSendInterceptService;

    @Inject
    public SmsService(
            SmsSendService smsSendService,
            SmsRepository smsRepository,
            SmsSendInterceptService smsSendInterceptService) {
        this.smsSendService = smsSendService;
        this.smsRepository = smsRepository;
        this.smsSendInterceptService = smsSendInterceptService;
    }

    public Future<Void> create(
            String mobile,
            NotificationCategoryType notificationCategoryType,
            Map<String, String> templateParameters,
            String notificationCategoryMetadata) {
        Sms sms = Sms.newSms(
                smsRepository.nextId(),
                mobile,
                templateParameters,
                notificationCategoryType,
                notificationCategoryMetadata);
        return smsRepository.add(sms);
    }

    public Future<Void> send(Sms sms) {
        return smsSendInterceptService
                .check()
                .compose(ignored -> sms.send(smsSendService))
                .compose(ignored -> smsRepository.save(sms));
    }
}

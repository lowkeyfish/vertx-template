/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification;

import com.yujunyang.vertx.template.web.domain.notification.sms.SmsService;
import io.vertx.core.Future;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class NotificationService {
    private SmsService smsService;

    @Inject
    public NotificationService(SmsService smsService) {
        this.smsService = smsService;
    }

    public Future<Void> sendSmsNotification(
            String mobile,
            NotificationCategoryType notificationCategoryType,
            Map<String, String> templateParameters,
            String notificationCategoryMetadata) {
        return smsService.create(mobile, notificationCategoryType, templateParameters, notificationCategoryMetadata);
    }
}

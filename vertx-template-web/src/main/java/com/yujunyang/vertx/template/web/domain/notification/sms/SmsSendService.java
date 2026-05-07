/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.yujunyang.vertx.template.web.domain.notification.NotificationVendorType;
import io.vertx.core.Future;

public interface SmsSendService {
    NotificationVendorType vendor();

    Future<String> send(Sms sms);
}

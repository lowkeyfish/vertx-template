/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.notification.persistence.db.model;

import java.time.LocalDateTime;

public record SmsDataModel(
        long id,
        String mobile,
        String templateParameters,
        int notificationCategory,
        String notificationCategoryMetadata,
        LocalDateTime createTime,
        int status,
        LocalDateTime sendTime,
        int notificationVendor,
        String sendDetails,
        String sendResponse) {}

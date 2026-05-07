/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

public record MaxSentCountSmsSendInterceptor(SmsQueryCondition queryCondition, int maxCount, String description) {
    public boolean isIntercepted(int currentCount) {
        return currentCount >= maxCount;
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.yujunyang.vertx.template.web.domain.notification.NotificationCategoryType;
import com.yujunyang.vertx.template.web.domain.notification.NotificationVendorType;
import java.time.LocalDateTime;
import java.util.Optional;

public record SmsQueryCondition(
        Optional<SmsId> id,
        Optional<String> mobile,
        Optional<SmsStatusType> status,
        Optional<NotificationCategoryType> notificationCategory,
        Optional<String> notificationCategoryMetadata,
        Optional<NotificationVendorType> notificationVendor,
        Optional<LocalDateTime> createTimeBegin,
        Optional<LocalDateTime> createTimeEnd,
        Optional<LocalDateTime> sendTimeBegin,
        Optional<LocalDateTime> sendTimeEnd,
        int limit,
        int offset) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Optional<SmsId> id = Optional.empty();
        private Optional<String> mobile = Optional.empty();
        private Optional<SmsStatusType> status = Optional.empty();
        private Optional<NotificationCategoryType> notificationCategory = Optional.empty();
        private Optional<String> notificationCategoryMetadata = Optional.empty();
        private Optional<NotificationVendorType> notificationVendor = Optional.empty();
        private Optional<LocalDateTime> createTimeBegin = Optional.empty();
        private Optional<LocalDateTime> createTimeEnd = Optional.empty();
        private Optional<LocalDateTime> sendTimeBegin = Optional.empty();
        private Optional<LocalDateTime> sendTimeEnd = Optional.empty();
        private int limit = 20;
        private int offset = 0;

        public SmsQueryCondition build() {
            return new SmsQueryCondition(
                    id,
                    mobile,
                    status,
                    notificationCategory,
                    notificationCategoryMetadata,
                    notificationVendor,
                    createTimeBegin,
                    createTimeEnd,
                    sendTimeBegin,
                    sendTimeEnd,
                    limit,
                    offset);
        }

        public Builder id(SmsId id) {
            this.id = Optional.of(id);
            return this;
        }

        public Builder mobile(String mobile) {
            this.mobile = Optional.of(mobile);
            return this;
        }

        public Builder status(SmsStatusType status) {
            this.status = Optional.of(status);
            return this;
        }

        public Builder notificationCategory(NotificationCategoryType notificationCategoryType) {
            this.notificationCategory = Optional.of(notificationCategoryType);
            return this;
        }

        public Builder notificationCategoryMetadata(String notificationCategoryMetadata) {
            this.notificationCategoryMetadata = Optional.of(notificationCategoryMetadata);
            return this;
        }

        public Builder notificationVendor(NotificationVendorType notificationVendor) {
            this.notificationVendor = Optional.of(notificationVendor);
            return this;
        }

        public Builder createTimeBegin(LocalDateTime createTimeBegin) {
            this.createTimeBegin = Optional.of(createTimeBegin);
            return this;
        }

        public Builder createTimeEnd(LocalDateTime createTimeEnd) {
            this.createTimeEnd = Optional.of(createTimeEnd);
            return this;
        }

        public Builder sendTimeBegin(LocalDateTime sendTimeBegin) {
            this.sendTimeBegin = Optional.of(sendTimeBegin);
            return this;
        }

        public Builder sendTimeEnd(LocalDateTime sendTimeEnd) {
            this.sendTimeEnd = Optional.of(sendTimeEnd);
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            return this;
        }

        public Builder offset(int offset) {
            this.offset = offset;
            return this;
        }
    }
}

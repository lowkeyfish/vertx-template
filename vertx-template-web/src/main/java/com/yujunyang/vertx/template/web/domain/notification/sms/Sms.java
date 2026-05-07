/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.yujunyang.vertx.template.common.event.DomainEventPublisher;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.DateTimeUtilsEnhance;
import com.yujunyang.vertx.template.web.domain.notification.NotificationCategoryType;
import com.yujunyang.vertx.template.web.domain.notification.NotificationVendorType;
import io.vertx.core.Future;
import java.time.LocalDateTime;
import java.util.Map;
import org.apache.commons.collections4.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Sms {
    private static final Logger LOGGER = LogManager.getLogger(Sms.class);

    private SmsId id;
    private String mobile;
    private Map<String, String> templateParameters;
    private NotificationCategoryType notificationCategory;
    private String notificationCategoryMetadata;
    private LocalDateTime sendTime;
    private LocalDateTime createTime;
    private NotificationVendorType notificationVendor;
    private SmsStatusType status;
    private String sendDetails;
    private String sendResponse;

    public static Sms newSms(
            SmsId id,
            String mobile,
            Map<String, String> templateParameters,
            NotificationCategoryType notificationCategory,
            String notificationCategoryMetadata) {
        Sms sms = new Sms(
                id,
                mobile,
                templateParameters,
                notificationCategory,
                notificationCategoryMetadata,
                null,
                LocalDateTime.now(),
                null,
                SmsStatusType.PENDING,
                null,
                null);

        DomainEventPublisher.publish(new SmsCreated(DateTimeUtilsEnhance.epochMilliSecond(), id.getId()));

        return sms;
    }

    public Sms(
            SmsId id,
            String mobile,
            Map<String, String> templateParameters,
            NotificationCategoryType notificationCategory,
            String notificationCategoryMetadata,
            LocalDateTime sendTime,
            LocalDateTime createTime,
            NotificationVendorType notificationVendor,
            SmsStatusType status,
            String sendDetails,
            String sendResponse) {
        this.id = id;
        this.mobile = mobile;
        this.templateParameters = templateParameters;
        this.notificationCategory = notificationCategory;
        this.notificationCategoryMetadata = notificationCategoryMetadata;
        this.sendTime = sendTime;
        this.createTime = createTime;
        this.notificationVendor = notificationVendor;
        this.status = status;
        this.sendDetails = sendDetails;
        this.sendResponse = sendResponse;
    }

    public Future<Void> send(SmsSendService smsSendService) {
        if (!SmsStatusType.PENDING.equals(this.status)) {
            return Future.succeededFuture();
        }

        return smsSendService
                .send(this)
                .compose(sendResponse -> {
                    this.status = SmsStatusType.SENT;
                    this.sendTime = LocalDateTime.now();
                    this.sendResponse = sendResponse;
                    this.notificationVendor = smsSendService.vendor();

                    DomainEventPublisher.publish(new SmsSent(DateTimeUtilsEnhance.epochMilliSecond(), id.getId()));
                    return Future.<Void>succeededFuture();
                })
                .recover(throwable -> {
                    if (throwable instanceof SystemException) {
                        this.status = SmsStatusType.SEND_FAILED;
                        this.sendTime = LocalDateTime.now();
                        LOGGER.error(((SystemException) throwable).getLogMessage(), throwable);
                        return Future.succeededFuture();
                    }
                    return Future.failedFuture(throwable);
                });
    }

    public SmsId getId() {
        return id;
    }

    public String getMobile() {
        return mobile;
    }

    public Map<String, String> getTemplateParameters() {
        return MapUtils.emptyIfNull(templateParameters);
    }

    public NotificationCategoryType getNotificationCategory() {
        return notificationCategory;
    }

    public String getNotificationCategoryMetadata() {
        return notificationCategoryMetadata;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public NotificationVendorType getNotificationVendor() {
        return notificationVendor;
    }

    public SmsStatusType getStatus() {
        return status;
    }

    public String getSendDetails() {
        return sendDetails;
    }

    public String getSendResponse() {
        return sendResponse;
    }

    private Future<Void> apply(Throwable throwable) {
        if (throwable instanceof SystemException) {
            this.status = SmsStatusType.SEND_FAILED;
            this.sendTime = LocalDateTime.now();
            LOGGER.error("短信发送出错:" + ((SystemException) throwable).getLogMessage(), throwable);
            return Future.succeededFuture();
        }
        return Future.failedFuture(throwable);
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.infrastructure.notification;

import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponseBody;
import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import com.yujunyang.vertx.template.web.config.AliyunConfig;
import com.yujunyang.vertx.template.web.config.ApplicationConfig;
import com.yujunyang.vertx.template.web.domain.notification.NotificationCategoryType;
import com.yujunyang.vertx.template.web.domain.notification.NotificationVendorType;
import com.yujunyang.vertx.template.web.domain.notification.sms.Sms;
import com.yujunyang.vertx.template.web.domain.notification.sms.SmsSendService;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AliyunSmsSendService implements SmsSendService {
    private AliyunConfig config;
    private AsyncClient client;

    @Inject
    public AliyunSmsSendService(ApplicationConfigProvider applicationConfigProvider, AsyncClient client) {
        ApplicationConfig applicationConfig = applicationConfigProvider.getConfig();
        this.config = applicationConfig.getAliyun();
        this.client = client;
    }

    @Override
    public NotificationVendorType vendor() {
        return NotificationVendorType.ALIYUN;
    }

    @Override
    public Future<String> send(Sms sms) {
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .phoneNumbers(sms.getMobile())
                .signName(config.getSmsSignName())
                .templateCode(templateCode(sms.getNotificationCategory()))
                .templateParam(JacksonUtils.serialize(sms.getTemplateParameters()))
                .outId(sms.getId().toString())
                .build();
        CompletableFuture<SendSmsResponse> sendSmsResponseCompletableFuture = client.sendSms(sendSmsRequest);
        return Future.fromCompletionStage(sendSmsResponseCompletableFuture, Vertx.currentContext())
                .compose(sendSmsResponse -> {
                    SendSmsResponseBody sendSmsResponseBody = sendSmsResponse.getBody();

                    CheckUtils.isTrue(
                            "OK".equalsIgnoreCase(sendSmsResponseBody.getCode()),
                            new SystemException(
                                    "阿里云短信发送失败",
                                    ErrorType.ALIYUN_ERROR,
                                    Map.of(
                                            "smsId",
                                            sms.getId(),
                                            "code",
                                            sendSmsResponseBody.getCode(),
                                            "message",
                                            sendSmsResponseBody.getMessage(),
                                            "sendResponse",
                                            JacksonUtils.serialize(sendSmsResponseBody))));

                    return Future.succeededFuture(JacksonUtils.serialize(sendSmsResponseBody));
                });
    }

    private String templateCode(NotificationCategoryType notificationCategoryType) {
        Map<String, String> smsTemplateCodes = config.getSmsTemplateCodes();
        String templateCode = Optional.ofNullable(smsTemplateCodes)
                .orElse(new HashMap<>())
                .get(notificationCategoryType.name().toUpperCase());
        CheckUtils.notBlank(
                templateCode,
                new SystemException(
                        "阿里云发送短信 NotificationCategory未配置对应的TemplateCode",
                        Map.of("notificationCategoryType", notificationCategoryType)));
        return templateCode;
    }
}

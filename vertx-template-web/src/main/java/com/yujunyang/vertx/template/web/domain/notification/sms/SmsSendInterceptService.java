/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.utils.JacksonUtils;
import io.vertx.core.Future;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.collections4.CollectionUtils;

@Singleton
public class SmsSendInterceptService {
    private SmsRepository smsRepository;

    @Inject
    public SmsSendInterceptService(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    public Future<Void> check() {
        List<MaxSentCountSmsSendInterceptor> interceptors = interceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            return Future.succeededFuture();
        }

        List<Future<Void>> futures = interceptors.stream()
                .map(interceptor -> smsRepository
                        .countByCondition(interceptor.queryCondition())
                        .compose(count -> interceptor.isIntercepted(count)
                                ? Future.<Void>failedFuture(new BusinessException(
                                        interceptor.description(),
                                        ErrorType.SMS_SEND_LIMIT_EXCEEDED,
                                        Map.of("interceptor", JacksonUtils.serialize(interceptor))))
                                : Future.succeededFuture()))
                .toList();

        return Future.all(futures).mapEmpty();
    }

    private List<MaxSentCountSmsSendInterceptor> interceptors() {
        return new ArrayList<>();
    }
}

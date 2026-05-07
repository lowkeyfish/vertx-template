/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.di;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.web.config.AliyunConfig;
import com.yujunyang.vertx.template.web.config.ApplicationConfig;
import dagger.Module;
import dagger.Provides;
import darabonba.core.client.ClientOverrideConfiguration;
import javax.inject.Singleton;

@Module
public class AliyunModule {

    @Provides
    @Singleton
    public AsyncClient provideSmsAsyncClient(ApplicationConfigProvider applicationConfigProvider) {
        ApplicationConfig applicationConfig = applicationConfigProvider.getConfig();
        AliyunConfig aliyunConfig = applicationConfig.getAliyun();
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(aliyunConfig.getAccessKeyId())
                .accessKeySecret(aliyunConfig.getAccessKeySecret())
                .build());
        AsyncClient client = AsyncClient.builder()
                .region(aliyunConfig.getSmsRegion())
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create().setEndpointOverride("dysmsapi.aliyuncs.com"))
                .build();
        return client;
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.jwt.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.common.config.DefaultApplicationConfig;
import com.yujunyang.vertx.template.common.config.JWTConfig;
import com.yujunyang.vertx.template.common.config.JWTConfigItem;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class JwtModule {
    @Provides
    @Singleton
    @Named("access")
    public JWTAuth provideAccessTokenJWTAuth(Vertx vertx, ApplicationConfigProvider applicationConfigProvider) {
        DefaultApplicationConfig config = applicationConfigProvider.getConfig();
        JWTConfig jwtConfig = config.jwt();
        JWTConfigItem jwtConfigItem = jwtConfig.access();
        JWTAuth provider = JWTAuth.create(
                vertx,
                new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm(jwtConfigItem.algorithm())
                                .setBuffer(jwtConfigItem.secret())));
        return provider;
    }

    @Provides
    @Singleton
    @Named("refresh")
    public JWTAuth provideRefreshTokenJWTAuth(Vertx vertx, ApplicationConfigProvider applicationConfigProvider) {
        DefaultApplicationConfig config = applicationConfigProvider.getConfig();
        JWTConfig jwtConfig = config.jwt();
        JWTConfigItem jwtConfigItem = jwtConfig.refresh();
        JWTAuth provider = JWTAuth.create(
                vertx,
                new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm(jwtConfigItem.algorithm())
                                .setBuffer(jwtConfigItem.secret())));
        return provider;
    }

    @Provides
    @Singleton
    @Named("temp")
    public JWTAuth provideTempTokenJWTAuth(Vertx vertx, ApplicationConfigProvider applicationConfigProvider) {
        DefaultApplicationConfig config = applicationConfigProvider.getConfig();
        JWTConfig jwtConfig = config.jwt();
        JWTConfigItem jwtConfigItem = jwtConfig.temp();
        JWTAuth provider = JWTAuth.create(
                vertx,
                new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm(jwtConfigItem.algorithm())
                                .setBuffer(jwtConfigItem.secret())));
        return provider;
    }
}

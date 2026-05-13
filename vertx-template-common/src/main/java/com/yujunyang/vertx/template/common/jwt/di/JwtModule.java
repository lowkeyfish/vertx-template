/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.jwt.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.common.config.DefaultApplicationConfig;
import com.yujunyang.vertx.template.common.config.JWTConfig;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import javax.inject.Singleton;

@Module
public class JwtModule {
    @Provides
    @Singleton
    public JWTAuth provideJWTAuth(Vertx vertx, ApplicationConfigProvider applicationConfigProvider) {
        DefaultApplicationConfig config = applicationConfigProvider.getConfig();
        JWTConfig jwtConfig = config.jwt();
        JWTAuth provider = JWTAuth.create(
                vertx,
                new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm(jwtConfig.algorithm())
                                .setBuffer(jwtConfig.secret())));
        return provider;
    }
}

package com.yujunyang.vertx.template.common.authentication.di;

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
public class AuthenticationModule {
    @Provides
    @Singleton
    public JWTAuth provideJWTAuth(Vertx vertx, ApplicationConfigProvider applicationConfigProvider) {
        DefaultApplicationConfig config = applicationConfigProvider.getConfig();
        JWTConfig jwtConfig = config.getJwt();
        JWTAuth provider = JWTAuth.create(
                vertx,
                new JWTAuthOptions()
                        .addPubSecKey(new PubSecKeyOptions()
                                .setAlgorithm(jwtConfig.getAlgorithm())
                                .setBuffer(jwtConfig.getSecret())));
        return provider;
    }
}

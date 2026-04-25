package com.yujunyang.vertx.template.common.config.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigManager;
import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.common.config.ApplicationConfigProviderImpl;
import com.yujunyang.vertx.template.common.config.DefaultApplicationConfig;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

@Module
public class ApplicationConfigModule {
    @Provides
    @Singleton
    public ApplicationConfigProvider provideApplicationConfigProvider() {
        DefaultApplicationConfig applicationConfig = ApplicationConfigManager.get();
        return new ApplicationConfigProviderImpl(applicationConfig);
    }
}

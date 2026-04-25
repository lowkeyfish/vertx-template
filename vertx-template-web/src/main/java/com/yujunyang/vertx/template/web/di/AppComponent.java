package com.yujunyang.vertx.template.web.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import dagger.Component;
import io.vertx.core.Vertx;
import javax.inject.Singleton;
import org.redisson.api.RedissonClient;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    ServiceComponent.Factory serviceComponent();

    Vertx getVertx();

    ApplicationConfigProvider getApplicationConfigProvider();

    RedissonClient getRedissonClient();
}

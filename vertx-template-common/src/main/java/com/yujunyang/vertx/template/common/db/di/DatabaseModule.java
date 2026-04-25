package com.yujunyang.vertx.template.common.db.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.common.config.DatasourceConfig;
import com.yujunyang.vertx.template.common.config.DefaultApplicationConfig;
import com.yujunyang.vertx.template.common.db.pool.PoolFactory;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Pool;
import javax.inject.Singleton;

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    public Pool providePool(Vertx vertx, ApplicationConfigProvider configProvider) {
        DefaultApplicationConfig config = configProvider.getConfig();
        DatasourceConfig datasourceConfig = config.getDatasource();
        return PoolFactory.createMySQLPool(vertx, datasourceConfig);
    }
}

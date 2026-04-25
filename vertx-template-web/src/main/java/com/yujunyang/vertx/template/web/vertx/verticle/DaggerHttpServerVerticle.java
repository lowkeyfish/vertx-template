package com.yujunyang.vertx.template.web.vertx.verticle;

import com.yujunyang.vertx.template.common.config.ApplicationConfigManager;
import com.yujunyang.vertx.template.common.log4j2.DataMessage;
import com.yujunyang.vertx.template.common.vertx.router.AllRouter;
import com.yujunyang.vertx.template.web.application.TestApplicationService;
import com.yujunyang.vertx.template.web.di.AppComponent;
import com.yujunyang.vertx.template.web.di.ServiceComponent;
import com.yujunyang.vertx.template.web.vertx.router.GraphQLRouter;
import com.yujunyang.vertx.template.web.vertx.router.HealthRouter;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.Router;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DaggerHttpServerVerticle extends VerticleBase {
    private static final Logger LOGGER = LogManager.getLogger(DaggerHttpServerVerticle.class);
    private AppComponent appComponent;

    public DaggerHttpServerVerticle(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    @Override
    public Future<?> start() {
        ServiceComponent serviceComponent = appComponent.serviceComponent().create();
        TestApplicationService testApplicationService = serviceComponent.getTestApplicationService();

        Router router = Router.router(vertx);
        new AllRouter().appendTo(router);
        new HealthRouter().appendTo(router);
        new GraphQLRouter().appendTo(vertx, router);
        int port = ApplicationConfigManager.get().getServer().getPort();
        return vertx.createHttpServer().requestHandler(router).listen(port).onSuccess(http -> {
            LOGGER.info(DataMessage.of("Http Server启动成功", Map.of("port", port)));
        });
    }
}

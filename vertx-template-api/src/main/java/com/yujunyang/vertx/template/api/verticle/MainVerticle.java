package com.yujunyang.vertx.template.api.verticle;

import com.yujunyang.vertx.template.api.web.router.TestRouter;
import com.yujunyang.vertx.template.common.log4j2.DataMessage;
import com.yujunyang.vertx.template.common.vertx.config.ApplicationConfigManager;
import com.yujunyang.vertx.template.common.vertx.router.AllRouter;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.Router;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainVerticle extends VerticleBase {
    private static final Logger LOGGER = LogManager.getLogger(MainVerticle.class);

    @Override
    public Future<?> start() {
        Router router = Router.router(vertx);
        new AllRouter().appendTo(router);
        new TestRouter().appendTo(router);
        int port = ApplicationConfigManager.get().getServer().getPort();
        return vertx.createHttpServer().requestHandler(router).listen(port).onSuccess(http -> {
            LOGGER.info(DataMessage.of("Sparrow Http Server启动成功", Map.of("port", port)));
        });
    }
}

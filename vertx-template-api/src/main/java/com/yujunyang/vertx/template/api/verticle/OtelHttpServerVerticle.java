/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.api.verticle;

import com.yujunyang.vertx.template.api.config.ApplicationConfig;
import com.yujunyang.vertx.template.api.web.router.OtelHttpServerRouter;
import com.yujunyang.vertx.template.common.log4j2.DataMessage;
import com.yujunyang.vertx.template.common.vertx.config.ApplicationConfigManager;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.Router;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OtelHttpServerVerticle extends VerticleBase {
    private static final Logger LOGGER = LogManager.getLogger(OtelHttpServerVerticle.class);

    @Override
    public Future<?> start() {
        Router router = Router.router(vertx);
        new OtelHttpServerRouter().appendTo(router);

        ApplicationConfig applicationConfig = ApplicationConfigManager.get();
        int port = applicationConfig.getOtel().getServer().getPort().getHttp();
        return vertx.createHttpServer().requestHandler(router).listen(port).onSuccess(http -> {
            LOGGER.info(DataMessage.of("OTel Http Server启动成功", Map.of("port", port)));
        });
    }
}

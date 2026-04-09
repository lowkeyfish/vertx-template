/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.vertx.verticle;

import com.yujunyang.vertx.template.web.vertx.router.GraphQLRouter;
import com.yujunyang.vertx.template.web.vertx.router.HealthRouter;
import com.yujunyang.vertx.template.common.log4j2.DataMessage;
import com.yujunyang.vertx.template.common.vertx.config.ApplicationConfigManager;
import com.yujunyang.vertx.template.common.vertx.router.AllRouter;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.ext.web.Router;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpServerVerticle extends VerticleBase {
    private static final Logger LOGGER = LogManager.getLogger(HttpServerVerticle.class);

    @Override
    public Future<?> start() {
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

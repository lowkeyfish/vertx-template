/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.vertx.router;

import graphql.GraphQL;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.graphql.GraphiQLHandler;
import io.vertx.ext.web.handler.graphql.GraphiQLHandlerOptions;

public class GraphQLRouter {
    public void appendTo(Vertx vertx, Router router) {
        // GraphQL graphQL = setupGraphQL();
        // router.post("/graphql").handler(GraphQLHandler.create(graphQL));
        graphiQLRoute(vertx, router);
    }

    private GraphQL setupGraphQL() {
        return null;
    }

    private void graphiQLRoute(Vertx vertx, Router router) {
        GraphiQLHandlerOptions options = new GraphiQLHandlerOptions().setEnabled(true);
        GraphiQLHandler handler = GraphiQLHandler.create(vertx, options);
        router.route("/graphiql*").subRouter(handler.router());
    }
}

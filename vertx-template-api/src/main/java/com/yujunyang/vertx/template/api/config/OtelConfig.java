/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.api.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OtelConfig {
    @JsonProperty("server")
    private Server server;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Server {
        @JsonProperty("port")
        private Port port;

        public Port getPort() {
            return port;
        }

        public void setPort(Port port) {
            this.port = port;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Port {
        @JsonProperty("http")
        private int http;

        @JsonProperty("grpc")
        private int grpc;

        public int getHttp() {
            return http;
        }

        public void setHttp(int http) {
            this.http = http;
        }

        public int getGrpc() {
            return grpc;
        }

        public void setGrpc(int grpc) {
            this.grpc = grpc;
        }
    }
}

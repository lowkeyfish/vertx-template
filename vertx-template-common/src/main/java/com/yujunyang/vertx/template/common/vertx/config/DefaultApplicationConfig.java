package com.yujunyang.vertx.template.common.vertx.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultApplicationConfig {
    @JsonProperty("vertx")
    private VertxConfig vertx;

    @JsonProperty("server")
    private ServerConfig server;

    @JsonProperty("datasource")
    private DatasourceConfig datasource;

    public VertxConfig getVertx() {
        return vertx;
    }

    public void setVertx(VertxConfig vertx) {
        this.vertx = vertx;
    }

    public ServerConfig getServer() {
        return server;
    }

    public void setServer(ServerConfig server) {
        this.server = server;
    }

    public DatasourceConfig getDatasource() {
        return datasource;
    }

    public void setDatasource(DatasourceConfig datasource) {
        this.datasource = datasource;
    }
}

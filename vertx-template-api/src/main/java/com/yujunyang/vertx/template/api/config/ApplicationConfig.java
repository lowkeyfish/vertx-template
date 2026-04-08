package com.yujunyang.vertx.template.api.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yujunyang.vertx.template.common.vertx.config.DefaultApplicationConfig;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApplicationConfig extends DefaultApplicationConfig {
    @JsonProperty("otel")
    private OtelConfig otel;

    public OtelConfig getOtel() {
        return otel;
    }

    public void setOtel(OtelConfig otel) {
        this.otel = otel;
    }
}

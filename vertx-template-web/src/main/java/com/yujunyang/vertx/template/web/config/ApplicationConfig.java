/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.config;

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

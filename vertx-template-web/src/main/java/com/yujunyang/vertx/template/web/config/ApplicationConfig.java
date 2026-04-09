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
    @JsonProperty("test")
    private int test;

    public int getTest() {
        return test;
    }

    public void setTest(int test) {
        this.test = test;
    }
}

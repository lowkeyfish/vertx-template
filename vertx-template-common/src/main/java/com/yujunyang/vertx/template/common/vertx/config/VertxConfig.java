package com.yujunyang.vertx.template.common.vertx.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VertxConfig {
    @JsonProperty("deploymentInstances")
    private int deploymentInstance;

    public int getDeploymentInstance() {
        return deploymentInstance;
    }

    public void setDeploymentInstance(int deploymentInstance) {
        this.deploymentInstance = deploymentInstance;
    }
}

package com.yujunyang.vertx.template.common.config;

public interface ApplicationConfigProvider {
    <T extends DefaultApplicationConfig> T getConfig();
}

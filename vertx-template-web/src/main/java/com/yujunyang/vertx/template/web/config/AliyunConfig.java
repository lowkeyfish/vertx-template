/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AliyunConfig {
    @JsonProperty("accessKeyId")
    private String accessKeyId;

    @JsonProperty("accessKeySecret")
    private String accessKeySecret;

    @JsonProperty("smsRegion")
    private String smsRegion;

    @JsonProperty("smsSignName")
    private String smsSignName;

    @JsonProperty("smsTemplateCodes")
    private Map<String, String> smsTemplateCodes;

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getSmsSignName() {
        return smsSignName;
    }

    public void setSmsSignName(String smsSignName) {
        this.smsSignName = smsSignName;
    }

    public Map<String, String> getSmsTemplateCodes() {
        return smsTemplateCodes;
    }

    public void setSmsTemplateCodes(Map<String, String> smsTemplateCodes) {
        this.smsTemplateCodes = smsTemplateCodes;
    }

    public String getSmsRegion() {
        return smsRegion;
    }

    public void setSmsRegion(String smsRegion) {
        this.smsRegion = smsRegion;
    }
}

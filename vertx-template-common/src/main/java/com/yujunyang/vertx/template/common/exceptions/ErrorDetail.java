/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorDetail(String field, String message, Map<String, Object> extra) {

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String field;
        private String message;
        private Map<String, Object> extra;

        private Builder() {}

        public Builder field(String field) {
            this.field = field;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder extra(Map<String, Object> extra) {
            this.extra = extra;
            return this;
        }

        public ErrorDetail build() {
            CheckUtils.isTrue(
                    StringUtils.isNotBlank(field)
                            || StringUtils.isNotBlank(message)
                            || (extra != null && !extra.isEmpty()),
                    "errorDetail属性未设置");

            return new ErrorDetail(field, message, extra);
        }
    }
}

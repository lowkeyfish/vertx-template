/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.notification.sms;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yujunyang.vertx.template.common.domain.id.AbstractLongId;

public class SmsId extends AbstractLongId {

    @JsonCreator
    public SmsId(@JsonProperty("id") Long id) {
        super(id);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SmsId parse(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return new SmsId(id);
    }

    @Override
    protected int initialNonZeroOddNumber() {
        return 11;
    }

    @Override
    protected int multiplierNonZeroOddNumber() {
        return 113;
    }
}

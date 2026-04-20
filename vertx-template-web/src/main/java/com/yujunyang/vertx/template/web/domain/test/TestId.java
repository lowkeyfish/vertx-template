/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yujunyang.vertx.template.common.domain.id.AbstractLongId;

public class TestId extends AbstractLongId {

    @JsonCreator
    public TestId(@JsonProperty("id") Long id) {
        super(id);
    }

    @Override
    protected int initialNonZeroOddNumber() {
        return 5;
    }

    @Override
    protected int multiplierNonZeroOddNumber() {
        return 55;
    }
}

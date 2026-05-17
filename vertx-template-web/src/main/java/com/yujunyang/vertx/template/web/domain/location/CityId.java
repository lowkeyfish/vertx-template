/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.location;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.yujunyang.vertx.template.common.domain.id.AbstractIntegerId;

public class CityId extends AbstractIntegerId {

    public CityId(Integer id) {
        super(id);
    }

    @Override
    protected int initialNonZeroOddNumber() {
        return 17;
    }

    @Override
    protected int multiplierNonZeroOddNumber() {
        return 257;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static CityId parse(Integer id) {
        if (id == null || id <= 0) {
            return null;
        }
        return new CityId(id);
    }
}

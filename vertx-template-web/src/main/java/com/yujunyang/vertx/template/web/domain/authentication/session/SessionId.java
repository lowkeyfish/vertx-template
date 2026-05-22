/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.authentication.session;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.yujunyang.vertx.template.common.domain.id.AbstractStringId;
import org.apache.commons.lang3.StringUtils;

public class SessionId extends AbstractStringId {
    public SessionId(String id) {
        super(id);
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static SessionId parse(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return new SessionId(id);
    }

    @Override
    protected int initialNonZeroOddNumber() {
        return 5;
    }

    @Override
    protected int multiplierNonZeroOddNumber() {
        return 415;
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.domain.id;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.github.yitter.idgen.YitIdHelper;

public final class IdUtils {

    private IdUtils() {}

    public static Long longId() {
        return YitIdHelper.nextId();
    }

    public static String stringId() {
        return NanoIdUtils.randomNanoId();
    }
}

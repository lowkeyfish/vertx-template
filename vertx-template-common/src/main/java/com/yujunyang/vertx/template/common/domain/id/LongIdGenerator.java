/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.domain.id;

import com.github.yitter.idgen.YitIdHelper;

public interface LongIdGenerator {
    default long nextLongId() {
        return YitIdHelper.nextId();
    }
}

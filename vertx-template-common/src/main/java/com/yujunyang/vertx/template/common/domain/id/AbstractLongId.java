/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.domain.id;

import com.yujunyang.vertx.template.common.utils.CheckUtils;

public abstract class AbstractLongId extends AbstractId<Long> {
    protected AbstractLongId(Long id) {
        super(id);
        CheckUtils.isTrue(id > 0, "id必须大于0");
    }
}

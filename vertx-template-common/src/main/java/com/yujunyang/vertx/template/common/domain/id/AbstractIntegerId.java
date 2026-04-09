/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.domain.id;

import com.yujunyang.vertx.template.common.utils.CheckUtils;

public abstract class AbstractIntegerId extends AbstractId<Integer> {
    protected AbstractIntegerId(Integer id) {
        super(id);
        CheckUtils.moreThan(id, 0, "id必须大于0");
    }
}

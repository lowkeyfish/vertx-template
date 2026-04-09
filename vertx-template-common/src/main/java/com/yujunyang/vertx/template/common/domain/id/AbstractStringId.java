/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.domain.id;

import com.yujunyang.vertx.template.common.utils.CheckUtils;

public abstract class AbstractStringId extends AbstractId<String> {
    protected AbstractStringId(String id) {
        super(id);
        CheckUtils.notBlank(id, "id不能为空");
    }
}

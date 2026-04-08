/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.enums;

/** 具有值枚举类型 */
public interface ValueEnum<T> {

    /**
     * 获取枚举值
     *
     * @return
     */
    T getValue();
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.domain.id;

import com.fasterxml.jackson.annotation.JsonValue;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

abstract class AbstractId<T> {
    @JsonValue
    private T id;

    protected AbstractId(T id) {
        CheckUtils.notNull(id, "id必须不为null");
        this.id = id;
    }

    public T getId() {
        return id;
    }

    @Override
    public boolean equals(Object anObject) {
        boolean ret = false;

        if (anObject != null && this.getClass() == anObject.getClass()) {
            AbstractId typedObject = (AbstractId) anObject;
            ret = this.getId().equals(typedObject.getId());
        }

        return ret;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(initialNonZeroOddNumber(), multiplierNonZeroOddNumber())
                .append(id)
                .toHashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }

    protected abstract int initialNonZeroOddNumber();

    protected abstract int multiplierNonZeroOddNumber();
}

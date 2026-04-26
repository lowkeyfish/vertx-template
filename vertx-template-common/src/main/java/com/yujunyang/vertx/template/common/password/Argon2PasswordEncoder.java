/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.password;

import com.password4j.Argon2Function;
import com.password4j.Hash;
import com.password4j.Password;
import com.password4j.types.Argon2;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Argon2PasswordEncoder implements PasswordEncoder {
    private static final Argon2Function ARGON2_FUNCTION = Argon2Function.getInstance(65536, 3, 4, 32, Argon2.ID);

    @Inject
    public Argon2PasswordEncoder() {}

    @Override
    public boolean canUse(PasswordEncodeType encodeType) {
        return PasswordEncodeType.ARGON2ID.equals(encodeType);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        CheckUtils.notBlank(rawPassword, "rawPassword不能为空");

        Hash hash = Password.hash(rawPassword).addRandomSalt().with(ARGON2_FUNCTION);
        return hash.getResult();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        CheckUtils.notBlank(rawPassword, "rawPassword不能为空");
        CheckUtils.notBlank(encodedPassword, "encodedPassword不能为空");

        return Password.check(rawPassword, encodedPassword).with(ARGON2_FUNCTION);
    }
}

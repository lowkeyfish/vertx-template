/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.authentication.password;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.password.Password;
import com.yujunyang.vertx.template.common.password.PasswordEncodeType;
import com.yujunyang.vertx.template.common.password.PasswordEncoder;
import com.yujunyang.vertx.template.common.password.PasswordValidator;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.inject.Singleton;

@Singleton
public class PasswordService {
    private Set<PasswordEncoder> passwordEncoders;
    private PasswordValidator passwordValidator;

    public PasswordService(Set<PasswordEncoder> passwordEncoders, PasswordValidator passwordValidator) {
        this.passwordEncoders = passwordEncoders;
        this.passwordValidator = passwordValidator;
    }

    public Password encodePassword(String rawPassword, Password currentPassword) {
        if (currentPassword != null) {
            PasswordEncoder passwordEncoder = passwordEncoder(currentPassword.hashAlgorithm());
            CheckUtils.isTrue(
                    !passwordEncoder.matches(rawPassword, currentPassword.hash()),
                    new BusinessException("新密码不能和当前密码相同", ErrorType.PASSWORD_UNCHANGED));
        }

        PasswordEncoder defaultPasswordEncoder = passwordEncoder(PasswordEncodeType.ARGON2ID);
        return new Password(defaultPasswordEncoder.encode(rawPassword), defaultPasswordEncoder.type());
    }

    public Password encodePassword(String rawPassword) {
        PasswordEncoder defaultPasswordEncoder = passwordEncoder(PasswordEncodeType.ARGON2ID);
        return new Password(defaultPasswordEncoder.encode(rawPassword), defaultPasswordEncoder.type());
    }

    private PasswordEncoder passwordEncoder(PasswordEncodeType passwordEncodeType) {
        Optional<PasswordEncoder> optionalPasswordEncoder = passwordEncoders.stream()
                .filter(n -> n.canUse(passwordEncodeType))
                .findFirst();
        CheckUtils.isTrue(
                optionalPasswordEncoder.isPresent(),
                new SystemException("未找到PasswordEncoder", Map.of("passwordEncodeType", passwordEncodeType.name())));
        return optionalPasswordEncoder.get();
    }

    public boolean passwordMatches(String rawPassword, Password password) {
        PasswordEncoder passwordEncoder = passwordEncoder(password.hashAlgorithm());
        return passwordEncoder.matches(rawPassword, password.hash());
    }
}

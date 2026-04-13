/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.password;

import com.yujunyang.vertx.template.common.exceptions.AppException;
import com.yujunyang.vertx.template.common.exceptions.ExceptionCodeType;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import java.util.Map;
import java.util.regex.Pattern;

public class DefaultPasswordValidator implements PasswordValidator {
    private static final String PATTERN_DIGIT = "\\d";
    private static final String PATTERN_UPPERCASE = "[A-Z]";
    private static final String PATTERN_LOWERCASE = "[a-z]";
    private static final String PATTERN_SPECIAL_CHARACTER = "[-~!@#$%^&*()_+={}\\[\\]|\\;:'\",<>./?]";

    @Override
    public void validate(CharSequence rawPassword) {
        CheckUtils.notBlank(rawPassword, "rawPassword不能为空");
        CheckUtils.isTrue(
                rawPassword.length() >= 8,
                new AppException(
                        "密码最少8个字符", ExceptionCodeType.PASSWORD_FORMAT_INVALID, Map.of("password", rawPassword)));
        CheckUtils.isTrue(
                rawPassword.length() <= 16,
                new AppException(
                        "密码最多16个字符", ExceptionCodeType.PASSWORD_FORMAT_INVALID, Map.of("password", rawPassword)));
        CheckUtils.isTrue(
                Pattern.matches(PATTERN_UPPERCASE, rawPassword),
                new AppException(
                        "密码至少需要包含一个大写字母", ExceptionCodeType.PASSWORD_FORMAT_INVALID, Map.of("password", rawPassword)));
        CheckUtils.isTrue(
                Pattern.matches(PATTERN_LOWERCASE, rawPassword),
                new AppException(
                        "密码至少需要包含一个小写字母", ExceptionCodeType.PASSWORD_FORMAT_INVALID, Map.of("password", rawPassword)));
        CheckUtils.isTrue(
                Pattern.matches(PATTERN_DIGIT, rawPassword),
                new AppException(
                        "密码至少需要包含一个数字", ExceptionCodeType.PASSWORD_FORMAT_INVALID, Map.of("password", rawPassword)));
        CheckUtils.isTrue(
                Pattern.matches(PATTERN_SPECIAL_CHARACTER, rawPassword),
                new AppException(
                        "密码至少需要包含一个特殊字符", ExceptionCodeType.PASSWORD_FORMAT_INVALID, Map.of("password", rawPassword)));
    }
}

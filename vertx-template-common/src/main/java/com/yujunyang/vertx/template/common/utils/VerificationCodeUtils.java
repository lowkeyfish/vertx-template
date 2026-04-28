/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.utils;

import java.util.Random;
import org.apache.commons.lang3.StringUtils;

public final class VerificationCodeUtils {
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final String SIX_DIGIT_VERIFICATION_CODE_PATTERN = "^\\d{6}$";

    public static String generateSixDigitVerificationCode() {
        String verificationCode = "";
        for (int i = 0; i < 6; i++) {
            verificationCode += String.valueOf(RANDOM.nextInt(10));
        }
        return verificationCode;
    }

    public static boolean verifyVerificationCodeFormat(String verificationCode) {
        return StringUtils.defaultString(verificationCode).matches(SIX_DIGIT_VERIFICATION_CODE_PATTERN);
    }
}

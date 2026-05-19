/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class SecretGenerateApplication {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        base64AESSecret();
    }

    private static void base64HS256Secret() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 32 字节 = 256 bits
        random.nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("HS256 Secret Key (Base64):");
        System.out.println(base64Key);
    }

    private static void base64AESSecret() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(base64Key);
    }
}

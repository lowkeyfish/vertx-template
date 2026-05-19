/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.security.crypto;

import com.yujunyang.vertx.template.common.exceptions.SystemException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public final class AesGcmUtils {
    private static final String ALGORITHM = "AES";
    private static final String AES_GCM_NO_PADDING = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int GCM_IV_LENGTH = 12; // bytes (recommended for GCM)
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private AesGcmUtils() {}

    /**
     * 加密：使用固定密码 + 随机盐/IV，并将盐和 IV 作为 AAD 保护
     *
     * @param plaintext 明文
     * @param base64AESSecret aes秘钥base64
     * @return Base64 编码的密文
     */
    public static String encrypt(String plaintext, String base64AESSecret) {
        try {
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            byte[] iv = new byte[GCM_IV_LENGTH];
            SECURE_RANDOM.nextBytes(iv);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey(base64AESSecret), spec);
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            byte[] combined = new byte[iv.length + ciphertext.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(ciphertext, 0, combined, iv.length, ciphertext.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new SystemException("aes加密出错:" + e.getMessage(), e);
        }
    }

    /**
     * 解密
     *
     * @param ciphertextBase64 base64格式密文
     * @param base64AESSecret aes秘钥base64
     * @return 原始明文
     */
    public static String decrypt(String ciphertextBase64, String base64AESSecret) {
        try {
            byte[] combined = Base64.getDecoder().decode(ciphertextBase64);
            if (combined.length < GCM_IV_LENGTH) {
                throw new IllegalArgumentException("Invalid ciphertext");
            }
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] ciphertext = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(combined, GCM_IV_LENGTH, ciphertext, 0, ciphertext.length);
            Cipher cipher = Cipher.getInstance(AES_GCM_NO_PADDING);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey(base64AESSecret), spec);
            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, java.nio.charset.StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SystemException("aes解密出错:" + e.getMessage(), e);
        }
    }

    /** 将 Base64 编码的密钥字符串转换为 SecretKey 对象 */
    private static SecretKey secretKey(String base64AESSecret) {
        byte[] keyBytes = Base64.getDecoder().decode(base64AESSecret);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /** 生成一个新的随机 AES-256 密钥（Base64 编码） */
    public static String base64AESSecret() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(256);
            SecretKey secretKey = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new SystemException("生成aes秘钥出错:" + e.getMessage(), e);
        }
    }
}

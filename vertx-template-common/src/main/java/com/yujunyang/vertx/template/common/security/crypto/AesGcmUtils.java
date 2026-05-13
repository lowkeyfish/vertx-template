/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.security.crypto;

import com.yujunyang.vertx.template.common.exceptions.SystemException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public final class AesGcmUtils {
    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128; // bits
    private static final int GCM_IV_LENGTH = 12; // bytes (recommended for GCM)
    private static final int SALT_LENGTH = 16; // bytes
    private static final int AES_KEY_SIZE = 256; // bits
    private static final int PBKDF2_ITERATIONS = 100_000;
    private static final String PBKDF2_ALG = "PBKDF2WithHmacSHA256";

    private AesGcmUtils() {}

    /**
     * 加密：使用固定密码 + 随机盐/IV，并将盐和 IV 作为 AAD 保护
     *
     * @param plaintext 明文
     * @param password aes秘钥生成密码
     * @return Base64 编码的密文（结构：salt + iv + ciphertext+tag）
     */
    public static String encrypt(String plaintext, String password) {
        try {
            // 生成随机盐和 IV
            byte[] salt = new byte[SALT_LENGTH];
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);
            random.nextBytes(iv);

            // 从密码和盐派生 AES 密钥
            SecretKey aesKey = deriveAesKey(password, salt);

            // 初始化 GCM Cipher
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);

            // 将盐和 IV 作为 AAD 附加认证数据（受标签保护）
            cipher.updateAAD(salt);
            cipher.updateAAD(iv);

            // 加密
            byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // 拼接: salt + iv + ciphertext（ciphertext 已包含 GCM 标签）
            byte[] combined = new byte[salt.length + iv.length + ciphertext.length];
            System.arraycopy(salt, 0, combined, 0, salt.length);
            System.arraycopy(iv, 0, combined, salt.length, iv.length);
            System.arraycopy(ciphertext, 0, combined, salt.length + iv.length, ciphertext.length);

            // Base64 编码
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new SystemException("aes加密出错:" + e.getMessage(), e);
        }
    }

    /**
     * 解密
     *
     * @param encryptedBase64 base64格式密文
     * @param password aes秘钥生成密码
     * @return 原始明文
     * @throws javax.crypto.AEADBadTagException 如果密文，salt，IV被篡改
     */
    public static String decrypt(String encryptedBase64, String password) {
        try {
            byte[] combined = Base64.getDecoder().decode(encryptedBase64);

            // 提取salt、IV、密文
            if (combined.length < SALT_LENGTH + GCM_IV_LENGTH) {
                throw new IllegalArgumentException("Invalid encrypted data");
            }
            byte[] salt = Arrays.copyOfRange(combined, 0, SALT_LENGTH);
            byte[] iv = Arrays.copyOfRange(combined, SALT_LENGTH, SALT_LENGTH + GCM_IV_LENGTH);
            byte[] ciphertext = Arrays.copyOfRange(combined, SALT_LENGTH + GCM_IV_LENGTH, combined.length);

            // 派生密钥
            SecretKey aesKey = deriveAesKey(password, salt);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            GCMParameterSpec spec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, aesKey, spec);

            // 设置AAD(salt,IV)
            cipher.updateAAD(salt);
            cipher.updateAAD(iv);

            byte[] plaintext = cipher.doFinal(ciphertext);
            return new String(plaintext, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new SystemException("aes解密出错:" + e.getMessage(), e);
        }
    }

    /**
     * PBKDF2派生AES密钥
     *
     * @param password aes秘钥生成密码
     * @param salt aes秘钥生成salt
     * @return aes秘钥
     * @throws Exception
     */
    private static SecretKey deriveAesKey(String password, byte[] salt) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALG);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, AES_KEY_SIZE);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), "AES");
        } catch (Exception e) {
            throw new SystemException("aes秘钥生成出错:" + e.getMessage(), e);
        }
    }
}

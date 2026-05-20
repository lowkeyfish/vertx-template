/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.jwt;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import javax.crypto.SecretKey;

public final class JWTUtils {
    private JWTUtils() {}

    public static SecretKey secretKey(String base64Secret) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        if (decodedKey.length != 32) {
            throw new SystemException("无效的jwt秘钥长度，应该32byte", ErrorType.CONFIG_ERROR);
        }
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public static Claims parseToken(String token, SecretKey secretKey) {
        CheckUtils.notBlank(token, new BusinessException("token不存在", ErrorType.AUTHENTICATION_TOKEN_INVALID));
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims;
        } catch (SignatureException e) {
            throw new BusinessException(
                    "token签名无效",
                    ErrorType.AUTHENTICATION_TOKEN_INVALID,
                    null,
                    Map.of("token", token, "reason", e.getMessage()),
                    e);
        } catch (ExpiredJwtException e) {
            throw new BusinessException(
                    "token已过期", ErrorType.AUTHENTICATION_TOKEN_EXPIRED, null, Map.of("token", token), e);
        } catch (Exception e) {
            throw new BusinessException(
                    "token无效",
                    ErrorType.AUTHENTICATION_TOKEN_INVALID,
                    null,
                    Map.of("token", token, "reason", e.getMessage()),
                    e);
        }
    }

    public static String base64SecretForHS256() {
        SecureRandom random = new SecureRandom();
        byte[] secretBytes = new byte[32];
        random.nextBytes(secretBytes);
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);
        return base64Secret;
    }
}

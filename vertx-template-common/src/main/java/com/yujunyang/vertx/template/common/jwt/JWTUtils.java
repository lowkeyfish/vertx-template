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
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParserBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.security.SecureRandom;
import java.sql.Date;
import java.time.Instant;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.apache.commons.lang3.StringUtils;

public final class JWTUtils {
    private JWTUtils() {}

    public static SecretKey secretKey(String base64Secret) {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        if (decodedKey.length != 32) {
            throw new SystemException("无效的jwt秘钥长度，应该32byte", ErrorType.CONFIG_ERROR);
        }
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public static String generateToken(
            String issuer,
            Set<String> audiences,
            SecretKey secretKey,
            int expiresInSeconds,
            String tokenId,
            Map<String, Object> claims) {
        JwtBuilder builder = Jwts.builder();
        if (StringUtils.isNotBlank(tokenId)) {
            builder.id(tokenId);
        }
        if (StringUtils.isNotBlank(issuer)) {
            builder.issuer(issuer);
        }
        if (audiences != null && !audiences.isEmpty()) {
            builder.audience().add(audiences);
        }
        return builder.issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(expiresInSeconds)))
                .claims(Optional.ofNullable(claims).orElse(new HashMap<>()))
                .signWith(secretKey)
                .compact();
    }

    public static Claims parseToken(String token, SecretKey secretKey, String requiredIssuer, String requiredAudience) {
        CheckUtils.notBlank(token, new BusinessException("token不存在", ErrorType.AUTHENTICATION_TOKEN_INVALID));
        try {
            JwtParserBuilder parser = Jwts.parser();
            if (StringUtils.isNotBlank(requiredIssuer)) {
                parser.requireIssuer(requiredIssuer);
            }
            if (StringUtils.isNotBlank(requiredAudience)) {
                parser.requireAudience(requiredAudience);
            }
            Claims claims = parser.verifyWith(secretKey)
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

    /** 验证token scope */
    public static void verifyScopes(Claims claims, Set<String> requiredScopes) {
        Set<String> scopes = extractScopes(claims);
        for (String required : requiredScopes) {
            if (!scopes.contains(required)) {
                throw new BusinessException(
                        "token缺少scope(" + required + ")",
                        ErrorType.AUTHORIZATION_TOKEN_SCOPE_INVALID,
                        Map.of("requiredScopes", requiredScopes, "tokenScope", scopes));
            }
        }
    }

    /** 提取scope */
    private static Set<String> extractScopes(Claims claims) {
        Object scopeObj = claims.get("scope");
        if (scopeObj == null) {
            return Set.of();
        }
        if (scopeObj instanceof String) {
            String scopeStr = (String) scopeObj;
            if (scopeStr.isBlank()) {
                return Set.of();
            }
            return Arrays.stream(scopeStr.split("\\s+")).collect(Collectors.toSet());
        } else if (scopeObj instanceof Iterable) {
            Set<String> scopes = new HashSet<>();
            for (Object item : (Iterable<?>) scopeObj) {
                if (item instanceof String) {
                    scopes.add((String) item);
                }
            }
            return scopes;
        }
        return Set.of();
    }

    public static String base64SecretForHS256() {
        SecureRandom random = new SecureRandom();
        byte[] secretBytes = new byte[32];
        random.nextBytes(secretBytes);
        String base64Secret = Base64.getEncoder().encodeToString(secretBytes);
        return base64Secret;
    }
}

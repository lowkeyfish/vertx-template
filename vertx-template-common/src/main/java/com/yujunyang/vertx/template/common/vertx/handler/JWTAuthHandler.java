package com.yujunyang.vertx.template.common.vertx.handler;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.jwt.JWTUtils;
import com.yujunyang.vertx.template.common.log4j2.DataMessage;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.vertx.core.Handler;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import java.util.Map;
import java.util.Set;
import javax.crypto.SecretKey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JWTAuthHandler implements Handler<RoutingContext> {
    private static final Logger LOGGER = LogManager.getLogger(JWTAuthHandler.class);

    private final SecretKey secretKey;
    private final String cookieName;
    private final boolean ignoreExpiration;
    private Set<String> scopes;

    /**
     * @param secretKey jwt密钥
     * @param cookieName 可选，从 Cookie 中读取 token 的名称（不使用时传 null）
     * @param ignoreExpiration 是否忽略过期时间（true 表示过期仍视为验证通过）
     */
    public JWTAuthHandler(SecretKey secretKey, String cookieName, boolean ignoreExpiration) {
        this.secretKey = secretKey;
        this.cookieName = cookieName;
        this.ignoreExpiration = ignoreExpiration;
    }

    /** 便捷构造，只从 Header 读取，不忽略过期。 */
    public JWTAuthHandler(SecretKey secretKey) {
        this(secretKey, null, false);
    }

    public JWTAuthHandler withScopes(Set<String> scopes) {
        CheckUtils.notNull(scopes, new SystemException("scopes不能为null"));
        this.scopes = Set.copyOf(scopes);
        return this;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String token = extractToken(routingContext);
        try {
            Claims claims = JWTUtils.parseToken(token, secretKey, null, null);
            JWTUtils.verifyScopes(claims, scopes);
            routingContext.put("jwtClaims", claims);
            routingContext.put("jwtExpired", false);
            routingContext.next();
        } catch (BusinessException e) {
            if (e.getError().getType().equalsIgnoreCase(ErrorType.AUTHENTICATION_TOKEN_EXPIRED.getType())) {
                ExpiredJwtException expiredJwtException = (ExpiredJwtException) e.getCause();
                // token 过期
                if (ignoreExpiration) {
                    // 忽略过期，将 claims 存入（可使用 e.getClaims()）
                    routingContext.put("jwtClaims", expiredJwtException.getClaims());
                    routingContext.put("jwtExpired", true);
                    routingContext.next();
                    return;
                } else {
                    // 仍然提供 claims 给后续 failureHandler 使用（例如清理 session）
                    routingContext.put("jwtExpiredClaims", expiredJwtException.getClaims());
                }
            }
            fail(routingContext, e);
        } catch (Exception e) {
            LOGGER.warn(DataMessage.of("token解析报错", Map.of("token", token, "reason", e.getMessage())), e);
            fail(
                    routingContext,
                    new BusinessException(
                            "认证失败",
                            ErrorType.AUTHENTICATION_FAILED,
                            null,
                            Map.of("token", token, "reason", e.getMessage()),
                            e));
        }
    }

    /** 从请求中提取 JWT 字符串。 优先从 Authorization Header 获取，如果不存在且 cookieName 不为空，则从 Cookie 获取。 */
    private String extractToken(RoutingContext routingContext) {
        // 从 Authorization header 提取
        String authHeader = routingContext.request().getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        // 从 Cookie 提取
        if (cookieName != null) {
            Cookie cookie = routingContext.request().getCookie(cookieName);
            if (cookie != null) {
                return cookie.getValue();
            }
        }
        return null;
    }

    private void fail(RoutingContext routingContext, BusinessException businessException) {
        routingContext.fail(businessException);
    }
}

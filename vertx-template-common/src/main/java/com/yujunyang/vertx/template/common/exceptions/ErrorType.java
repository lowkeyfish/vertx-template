/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.exceptions;

/**
 * 400 输入参数格式无效； 例如： 手机号格式无效； 邮箱格式无效；
 *
 * <p>401 身份认证异常； 403 权限验证异常；
 *
 * <p>404 资源不存在； 例如： 员工不存在；
 *
 * <p>409 操作冲突； 例如： 余额不足； 数据版本无效；
 *
 * <p>422 输入数据格式有效但业务规则不满足； 例如： 手机号已注册；
 *
 * <p>429 频率限流相关； 例如： 验证码发送次数限制；
 *
 * <p>500 未预期的代码报错；可预期主动抛出的系统错误；
 */
public enum ErrorType {
    VALIDATION_FAILED(400),
    VALIDATION_PASSWORD_FORMAT_INVALID(400),

    AUTHENTICATION_FAILED(401),
    AUTHORIZATION_FAILED(403),

    RESOURCE_NOT_FOUND(404),

    VERSION_CONFLICT(409),

    ENTERPRISE_NAME_EXISTS(422),

    RATE_LIMIT_EXCEEDED(429),
    VERIFICATION_CODE_SEND_LIMIT_EXCEEDED(429),

    INTERNAL_SERVER_ERROR(500),
    REDISSON_LOCK_ERROR(500);

    private int code;

    ErrorType(int httpStatusCode) {
        this.code = httpStatusCode;
    }

    public int getCode() {
        return code;
    }

    public String getType() {
        return this.name();
    }
}

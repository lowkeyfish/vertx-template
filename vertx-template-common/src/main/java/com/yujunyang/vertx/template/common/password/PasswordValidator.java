package com.yujunyang.vertx.template.common.password;

public interface PasswordValidator {
    void validate(CharSequence rawPassword);
}

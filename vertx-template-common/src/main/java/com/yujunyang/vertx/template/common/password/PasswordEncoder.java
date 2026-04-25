package com.yujunyang.vertx.template.common.password;

public interface PasswordEncoder {
    boolean canUse(PasswordEncodeType encodeType);

    String encode(CharSequence rawPassword);

    boolean matches(CharSequence rawPassword, String encodedPassword);
}

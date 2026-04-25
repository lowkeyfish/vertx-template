package com.yujunyang.vertx.template.common.password.di;

import com.yujunyang.vertx.template.common.password.Argon2PasswordEncoder;
import com.yujunyang.vertx.template.common.password.DefaultPasswordValidator;
import com.yujunyang.vertx.template.common.password.PasswordEncoder;
import com.yujunyang.vertx.template.common.password.PasswordValidator;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public abstract class PasswordModule {
    @Binds
    @IntoSet
    abstract PasswordEncoder bindArgon2PasswordEncoder(Argon2PasswordEncoder passwordEncoder);

    @Binds
    abstract PasswordValidator bindDefaultPasswordValidator(DefaultPasswordValidator passwordValidator);
}

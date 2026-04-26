/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.redis.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.common.config.RedisConfig;
import dagger.Module;
import dagger.Provides;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

@Module
public class RedisModule {
    @Provides
    public RedissonClient provideRedissonClient(ApplicationConfigProvider applicationConfigProvider) {
        RedisConfig redisConfig = applicationConfigProvider.getConfig().getRedis();
        Config config = new Config();
        config.setPassword(redisConfig.getPassword());
        config.useSingleServer().setAddress(redisConfig.getAddress());
        RedissonClient redissonClient = Redisson.create(config);
        return redissonClient;
    }
}

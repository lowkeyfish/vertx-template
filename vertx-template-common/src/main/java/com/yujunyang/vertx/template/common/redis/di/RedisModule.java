/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.redis.di;

import com.yujunyang.vertx.template.common.config.ApplicationConfigProvider;
import com.yujunyang.vertx.template.common.config.RedisConfig;
import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;
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

    @Provides
    public Redis provideRedis(Vertx vertx, ApplicationConfigProvider applicationConfigProvider) {
        RedisConfig redisConfig = applicationConfigProvider.getConfig().getRedis();
        RedisOptions options =
                new RedisOptions().setConnectionString(redisConfig.getAddress()).setPassword(redisConfig.getPassword());

        Redis redis = Redis.createClient(vertx, options);
        return redis;
    }

    @Provides
    public RedisAPI provideRedisAPI(Redis redis) {
        return RedisAPI.api(redis);
    }
}

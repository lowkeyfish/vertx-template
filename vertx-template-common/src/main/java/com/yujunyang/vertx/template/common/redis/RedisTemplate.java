/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.redis;

import com.yujunyang.vertx.template.common.exceptions.LockAcquisitionFailedException;
import com.yujunyang.vertx.template.common.utils.RedisUtils;
import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

@Singleton
public class RedisTemplate {
    private static final Logger LOGGER = LogManager.getLogger(RedisTemplate.class);
    private static final String KEY_LOCK_PREFIX = "__LOCK_";
    private RedissonClient redissonClient;
    private Vertx vertx;

    @Inject
    public RedisTemplate(RedissonClient redissonClient, Vertx vertx) {
        this.redissonClient = redissonClient;
        this.vertx = vertx;
    }

    public <T> Future<T> withLock(String key, Supplier<Future<T>> action) {
        String lockKey = generateLockKey(key);
        RLock lock = redissonClient.getLock(lockKey);

        Context context = Vertx.currentContext();
        Future<Boolean> tryLockFuture = Future.fromCompletionStage(lock.tryLockAsync(), context);
        return tryLockFuture.compose(acquired -> {
            if (!acquired) {
                return Future.failedFuture(new LockAcquisitionFailedException("锁获取失败: " + lockKey));
            }
            return action.get().eventually(() -> Future.fromCompletionStage(lock.unlockAsync(), context)
                    .onFailure(ex -> LOGGER.error("Redisson释放锁({})失败: {}", lockKey, ex.getMessage(), ex)));
        });
    }

    private String generateLockKey(String key) {
        return RedisUtils.generateKey(KEY_LOCK_PREFIX, key);
    }
}

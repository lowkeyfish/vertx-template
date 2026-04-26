/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.redis;

import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.LockAcquisitionFailedException;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.RedisUtils;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import java.text.MessageFormat;
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
        Promise<T> promise = Promise.promise();
        String lockKey = generateLockKey(key);
        RLock lock = redissonClient.getLock(lockKey);
        lock.tryLockAsync()
                .thenAccept(acquired -> {
                    if (acquired) {
                        vertx.runOnContext(v -> action.get().onComplete(ar -> {
                            lock.unlockAsync()
                                    .thenRun(() -> {
                                        if (ar.succeeded()) {
                                            promise.complete(ar.result());
                                        } else {
                                            promise.fail(ar.cause());
                                        }
                                    })
                                    .exceptionally(ex -> {
                                        if (ar.succeeded()) {
                                            promise.complete(ar.result());
                                        } else {
                                            promise.fail(ar.cause());
                                        }
                                        LOGGER.error("Redisson释放锁({})错误:{}", lockKey, ex.getMessage());
                                        return null;
                                    });
                        }));
                    } else {
                        promise.fail(new LockAcquisitionFailedException());
                    }
                })
                .exceptionally(ex -> {
                    String errorMessage = MessageFormat.format("Redisson获取锁({0})出错:{1}", lockKey, ex.getMessage());
                    promise.fail(new SystemException(errorMessage, ErrorType.INTERNAL_SERVER_ERROR, ex));
                    return null;
                });
        return promise.future();
    }

    private String generateLockKey(String key) {
        return RedisUtils.generateKey(KEY_LOCK_PREFIX, key);
    }
}

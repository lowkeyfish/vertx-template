/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.web.domain.authentication.verificationcode;

import com.yujunyang.vertx.template.common.exceptions.BusinessException;
import com.yujunyang.vertx.template.common.exceptions.ErrorType;
import com.yujunyang.vertx.template.common.exceptions.SystemException;
import com.yujunyang.vertx.template.common.utils.CheckUtils;
import com.yujunyang.vertx.template.common.utils.RedisUtils;
import com.yujunyang.vertx.template.common.utils.VerificationCodeUtils;
import com.yujunyang.vertx.template.web.domain.notification.NotificationCategoryType;
import com.yujunyang.vertx.template.web.domain.notification.NotificationChannelType;
import com.yujunyang.vertx.template.web.domain.notification.sms.SmsService;
import io.vertx.core.Future;
import io.vertx.redis.client.Command;
import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.Request;
import io.vertx.redis.client.Response;
import io.vertx.redis.client.ResponseType;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.lang3.StringUtils;

@Singleton
public class VerificationCodeService {
    private static final int TARGET_PER_MINUTE_SEND_COUNT_MAX = 1;
    private static final int TARGET_PER_DAY_SEND_COUNT_MAX = 10;
    private static final int IP_PER_MINUTE_SEND_COUNT_MAX = 10;
    private static final int IP_PER_DAY_SEND_COUNT_MAX = 1000;
    private static final String TARGET_TYPE_IP = "ip";
    private static final String PER_MINUTE_PERIOD_TYPE = "per_minute";
    private static final String PER_DAY_PERIOD_TYPE = "per_day";

    private SmsService smsService;
    private RedisAPI redisAPI;
    private Redis redis;

    @Inject
    public VerificationCodeService(SmsService smsService, RedisAPI redisAPI, Redis redis) {
        this.smsService = smsService;
        this.redisAPI = redisAPI;
        this.redis = redis;
    }

    /**
     * 发送验证码
     *
     * @param verificationCodeType 验证码类型，例如注册验证码、登录验证码
     * @param notificationChannelType 通知的类型，就是验证码通过什么方式发送，例如短信、邮箱
     * @param target 发送对象，因通知类型不同而不同，可以是手机号、邮箱
     * @param ip 客户端ip地址，用于通过ip限制验证码发送量
     * @return 超过发送次数限制返回BusinessException，发送失败或缓存更新失败返回SystemException
     */
    public Future<Void> send(
            VerificationCodeType verificationCodeType,
            NotificationChannelType notificationChannelType,
            String target,
            String ip) {
        return checkVerificationCodeSendCountLimit(verificationCodeType, notificationChannelType, target, ip)
                .compose(ignored -> {
                    String verificationCode = VerificationCodeUtils.generateSixDigitVerificationCode();
                    return executeSend(verificationCodeType, notificationChannelType, verificationCode, target)
                            .map(verificationCode);
                })
                .compose(verificationCode -> updateVerificationCodeCache(
                        verificationCodeType, notificationChannelType, target, ip, verificationCode));
    }

    /**
     * 发送验证码 SMS：创建短信并持久化
     *
     * @param verificationCodeType 验证码类型
     * @param notificationChannelType 发送方式
     * @param verificationCode 验证码
     * @param target 目标对象
     * @return
     */
    private Future<Void> executeSend(
            VerificationCodeType verificationCodeType,
            NotificationChannelType notificationChannelType,
            String verificationCode,
            String target) {
        if (NotificationChannelType.SMS.equals(notificationChannelType)) {
            return smsService.create(target, convert(verificationCodeType), Map.of("code", verificationCode), "");
        }

        return Future.failedFuture(
                new SystemException("不支持的验证码发送方式", Map.of("notificationChannelType", notificationChannelType)));
    }

    /**
     * 将验证码类型转换为通知类型<br>
     * 为什么不直接用通知类型：验证码类型是直接让调用端指定的，验证码类型实际上是通知类型的对照值的子集， 如果直接使用通知类型，还需要在验证码场景下判断传参是否是非验证码的，如果漏做可能会导致使用了不该用的通知类型
     *
     * @param verificationCodeType 验证码类型
     * @return 通知类型，如果没有对应值就是代码问题，抛出SystemException
     */
    private NotificationCategoryType convert(VerificationCodeType verificationCodeType) {
        NotificationCategoryType notificationCategoryType;
        switch (verificationCodeType) {
            case SIGN_UP:
                notificationCategoryType = NotificationCategoryType.SIGN_UP_VERIFICATION_CODE;
                break;
            case SIGN_IN:
                notificationCategoryType = NotificationCategoryType.SIGN_IN_VERIFICATION_CODE;
                break;
            default:
                notificationCategoryType = null;
                break;
        }

        CheckUtils.notNull(
                notificationCategoryType,
                new SystemException(
                        "VerificationCodeType无对应NotificationCategoryType",
                        Map.of("verificationCodeType", verificationCodeType)));

        return notificationCategoryType;
    }

    /**
     * 验证验证码是否有效
     *
     * @param verificationCodeType 验证码类型
     * @param notificationChannelType 发送方式
     * @param target 目标对象
     * @param verificationCode 验证码
     * @return 验证失败返回BusinessException
     */
    public Future<Void> verify(
            VerificationCodeType verificationCodeType,
            NotificationChannelType notificationChannelType,
            String target,
            String verificationCode) {
        String redisKey = verificationCodeRedisKey(verificationCodeType, notificationChannelType, target);
        return redisAPI.get(redisKey).compose(response -> {
            String verificationCodeCache =
                    Optional.ofNullable(response).map(r -> r.toString()).orElse(null);
            return StringUtils.isNotBlank(verificationCodeCache)
                            && verificationCodeCache.equalsIgnoreCase(verificationCode)
                    ? Future.succeededFuture()
                    : Future.failedFuture(new BusinessException(
                            "验证码错误",
                            ErrorType.VERIFICATION_CODE_INVALID,
                            Map.of(
                                    "verificationCodeRedisKey",
                                    redisKey,
                                    "verificationCodeCache",
                                    StringUtils.defaultString(verificationCodeCache),
                                    "verificationCode",
                                    verificationCode)));
        });
    }

    /**
     * 清理已使用过的验证码缓存<br>
     * 没有放到verify中，让更外层的调用位置在验证成功后主动调用清理
     *
     * @param verificationCodeType 验证码类型
     * @param notificationChannelType 通知方式
     * @param target 目标对象
     * @param verificationCode 要清理的缓存保存的验证码
     * @return
     */
    public Future<Void> clearUsedVerificationCode(
            VerificationCodeType verificationCodeType,
            NotificationChannelType notificationChannelType,
            String target,
            String verificationCode) {
        String redisKey = verificationCodeRedisKey(verificationCodeType, notificationChannelType, target);
        return redisAPI.get(redisKey)
                .compose(response -> {
                    boolean needDel = Optional.ofNullable(response)
                            .map(r -> r.toString().equalsIgnoreCase(verificationCode))
                            .orElse(false);
                    if (needDel) {
                        return redisAPI.del(List.of(redisKey));
                    }
                    return Future.succeededFuture();
                })
                .mapEmpty();
    }

    /**
     * 更新验证码缓存<br>
     * 主要包含三类缓存<br>
     * 1.目标对象已发送的验证码，用于后续验证验证码是否有效<br>
     * 2.目标对象时间段内发送的验证码记录，用于后续验证目标对象时间段内验证码数量是否已超过发送限制<br>
     * 3.客户端ip时间段内发送的验证码记录，用于后续验证ip时间段内验证码数量是否已超过发送限制<br>
     *
     * @param verificationCodeType 验证码类型
     * @param notificationChannelType 验证码发送方式
     * @param target 目标对象
     * @param ip 客户端ip
     * @param verificationCode 当前发送的验证码内容
     * @return 如果Redis批量操作存在失败命令，返回SystemException
     */
    private Future<Void> updateVerificationCodeCache(
            VerificationCodeType verificationCodeType,
            NotificationChannelType notificationChannelType,
            String target,
            String ip,
            String verificationCode) {
        String verificationCodeRedisKey =
                verificationCodeRedisKey(verificationCodeType, notificationChannelType, target);
        Instant now = Instant.now();
        String perMinuteTargetVerificationCodeSendRecordRedisKey = verificationCodeSendRecordRedisKey(
                verificationCodeType, notificationChannelType.name().toLowerCase(), target, PER_MINUTE_PERIOD_TYPE);
        String perDayTargetVerificationCodeSendRecordRedisKey = verificationCodeSendRecordRedisKey(
                verificationCodeType, notificationChannelType.name().toLowerCase(), target, PER_DAY_PERIOD_TYPE);
        String perMinuteIpVerificationCodeSendRecordRedisKey =
                verificationCodeSendRecordRedisKey(verificationCodeType, TARGET_TYPE_IP, ip, PER_MINUTE_PERIOD_TYPE);
        String perDayIpVerificationCodeSendRecordRedisKey =
                verificationCodeSendRecordRedisKey(verificationCodeType, TARGET_TYPE_IP, ip, PER_DAY_PERIOD_TYPE);
        long perMinuteVerificationCodeSendRecordMin =
                now.minus(Duration.ofMinutes(1)).getEpochSecond();
        long perDayVerificationCodeSendRecordMin = now.minus(Duration.ofDays(1)).getEpochSecond();

        List<Request> commands = List.of(
                // 设置已发送验证码的缓存，缓存时间10分钟
                Request.cmd(Command.SETEX)
                        .arg(verificationCodeRedisKey)
                        .arg(Duration.ofMinutes(10).getSeconds())
                        .arg(verificationCode),
                // 目标对象每分钟发送验证码记录缓存增加，使用 sorted set存储验证码发送记录，score用当前时间秒，值也用当前时间秒
                Request.cmd(Command.ZADD)
                        .arg(perMinuteTargetVerificationCodeSendRecordRedisKey)
                        .arg(now.getEpochSecond())
                        .arg(String.valueOf(now.getEpochSecond())),
                // 目标对象每分钟发送验证码记录缓存清理，清理掉当前时间1分钟前的记录
                Request.cmd(Command.ZREMRANGEBYSCORE)
                        .arg(perMinuteTargetVerificationCodeSendRecordRedisKey)
                        .arg(0)
                        .arg(perMinuteVerificationCodeSendRecordMin),
                // 设置目标对象每分钟发送验证码记录缓存有效期10分钟
                Request.cmd(Command.EXPIRE)
                        .arg(perMinuteTargetVerificationCodeSendRecordRedisKey)
                        .arg(Duration.ofMinutes(10).getSeconds()),
                // 目标对象每天发送验证码记录缓存增加，使用 sorted set存储验证码发送记录，score用当前时间秒，值也用当前时间秒
                Request.cmd(Command.ZADD)
                        .arg(perDayTargetVerificationCodeSendRecordRedisKey)
                        .arg(now.getEpochSecond())
                        .arg(String.valueOf(now.getEpochSecond())),
                // 目标对象每天发送验证码记录缓存数据清理，清理掉当前时间1天前的记录
                Request.cmd(Command.ZREMRANGEBYSCORE)
                        .arg(perDayTargetVerificationCodeSendRecordRedisKey)
                        .arg(0)
                        .arg(perDayVerificationCodeSendRecordMin),
                // 设置目标对象每天发送验证码记录缓存有效期1天
                Request.cmd(Command.EXPIRE)
                        .arg(perDayTargetVerificationCodeSendRecordRedisKey)
                        .arg(Duration.ofDays(1).getSeconds()),
                // ip每分钟发送验证码记录缓存增加，使用 sorted set存储验证码发送记录，score用当前时间秒，值也用当前时间秒
                Request.cmd(Command.ZADD)
                        .arg(perMinuteIpVerificationCodeSendRecordRedisKey)
                        .arg(now.getEpochSecond())
                        .arg(String.valueOf(now.getEpochSecond())),
                // ip每分钟发送验证码记录缓存清理，清理掉当前时间1分钟前的记录
                Request.cmd(Command.ZREMRANGEBYSCORE)
                        .arg(perMinuteIpVerificationCodeSendRecordRedisKey)
                        .arg(0)
                        .arg(perMinuteVerificationCodeSendRecordMin),
                // 设置ip每分钟发送验证码记录缓存有效期10分钟
                Request.cmd(Command.EXPIRE)
                        .arg(perMinuteIpVerificationCodeSendRecordRedisKey)
                        .arg(Duration.ofMinutes(10).getSeconds()),
                // ip每天发送验证码记录缓存增加，使用 sorted set存储验证码发送记录，score用当前时间秒，值也用当前时间秒
                Request.cmd(Command.ZADD)
                        .arg(perDayIpVerificationCodeSendRecordRedisKey)
                        .arg(now.getEpochSecond())
                        .arg(String.valueOf(now.getEpochSecond())),
                // ip每天发送验证码记录缓存数据清理，清理掉当前时间1天前的记录
                Request.cmd(Command.ZREMRANGEBYSCORE)
                        .arg(perDayIpVerificationCodeSendRecordRedisKey)
                        .arg(0)
                        .arg(perDayVerificationCodeSendRecordMin),
                // 设置ip每天发送验证码记录缓存有效期1天
                Request.cmd(Command.EXPIRE)
                        .arg(perDayIpVerificationCodeSendRecordRedisKey)
                        .arg(Duration.ofDays(1).getSeconds()));

        return redis.batch(commands).compose(responses -> {
            for (int i = 0; i < responses.size(); i++) {
                Response response = responses.get(i);
                if (response == null || response.type() == ResponseType.ERROR) {
                    return Future.failedFuture(new SystemException(
                            "updateVerificationCodeCache批量操作Redis存在失败命令",
                            Map.of("commandIndex", i, "response", response)));
                }
            }
            return Future.succeededFuture();
        });
    }

    /**
     * 检查验证码发送次数是否超出
     *
     * @param verificationCodeType 验证码类型
     * @param notificationChannelType 通知方式
     * @param target 目标对象
     * @param ip 客户端ip
     * @return 如果超出次数限制，使用BusinessException返回
     */
    private Future<Void> checkVerificationCodeSendCountLimit(
            VerificationCodeType verificationCodeType,
            NotificationChannelType notificationChannelType,
            String target,
            String ip) {
        Instant now = Instant.now();
        long perMinuteVerificationCodeSendRecordMin =
                now.minus(Duration.ofMinutes(1)).getEpochSecond();
        long perMinuteVerificationCodeSendRecordMax = now.getEpochSecond();
        long perDayVerificationCodeSendRecordMin = now.minus(Duration.ofDays(1)).getEpochSecond();
        long perDayVerificationCodeSendRecordMax = now.getEpochSecond();

        String perMinuteTargetVerificationCodeSendRecordRedisKey = verificationCodeSendRecordRedisKey(
                verificationCodeType, notificationChannelType.name().toLowerCase(), target, PER_MINUTE_PERIOD_TYPE);
        String perDayTargetVerificationCodeSendRecordRedisKey = verificationCodeSendRecordRedisKey(
                verificationCodeType, notificationChannelType.name().toLowerCase(), target, PER_DAY_PERIOD_TYPE);
        String perMinuteIpVerificationCodeSendRecordRedisKey =
                verificationCodeSendRecordRedisKey(verificationCodeType, TARGET_TYPE_IP, ip, PER_MINUTE_PERIOD_TYPE);
        String perDayIpVerificationCodeSendRecordRedisKey =
                verificationCodeSendRecordRedisKey(verificationCodeType, TARGET_TYPE_IP, ip, PER_DAY_PERIOD_TYPE);

        List<Request> commands = List.of(
                Request.cmd(Command.ZCOUNT)
                        .arg(perMinuteTargetVerificationCodeSendRecordRedisKey)
                        .arg(perMinuteVerificationCodeSendRecordMin)
                        .arg(perMinuteVerificationCodeSendRecordMax),
                Request.cmd(Command.ZCOUNT)
                        .arg(perDayTargetVerificationCodeSendRecordRedisKey)
                        .arg(perDayVerificationCodeSendRecordMin)
                        .arg(perDayVerificationCodeSendRecordMax),
                Request.cmd(Command.ZCOUNT)
                        .arg(perMinuteIpVerificationCodeSendRecordRedisKey)
                        .arg(perMinuteVerificationCodeSendRecordMin)
                        .arg(perMinuteVerificationCodeSendRecordMax),
                Request.cmd(Command.ZCOUNT)
                        .arg(perDayIpVerificationCodeSendRecordRedisKey)
                        .arg(perDayVerificationCodeSendRecordMin)
                        .arg(perDayVerificationCodeSendRecordMax));
        return redis.batch(commands).compose(responses -> {
            long targetPerMinuteSendCount = responses.get(0).toLong();
            long targetPerDaySendCount = responses.get(1).toLong();
            long ipPerMinuteSendCount = responses.get(2).toLong();
            long ipPerDaySendCount = responses.get(3).toLong();
            CheckUtils.isTrue(
                    targetPerMinuteSendCount < TARGET_PER_MINUTE_SEND_COUNT_MAX,
                    new BusinessException(
                            "验证码发送频率过快",
                            ErrorType.VERIFICATION_CODE_SEND_LIMIT_EXCEEDED,
                            Map.of(
                                    "redisKey",
                                    perMinuteTargetVerificationCodeSendRecordRedisKey,
                                    "currentCount",
                                    targetPerMinuteSendCount,
                                    "maxCount",
                                    TARGET_PER_MINUTE_SEND_COUNT_MAX)));

            CheckUtils.isTrue(
                    targetPerDaySendCount < TARGET_PER_DAY_SEND_COUNT_MAX,
                    new BusinessException(
                            "今日发送次数已达上限",
                            ErrorType.VERIFICATION_CODE_SEND_LIMIT_EXCEEDED,
                            Map.of(
                                    "redisKey",
                                    perDayTargetVerificationCodeSendRecordRedisKey,
                                    "currentCount",
                                    targetPerDaySendCount,
                                    "maxCount",
                                    TARGET_PER_DAY_SEND_COUNT_MAX)));

            CheckUtils.isTrue(
                    ipPerMinuteSendCount < IP_PER_MINUTE_SEND_COUNT_MAX,
                    new BusinessException(
                            "验证码发送频率过快",
                            ErrorType.VERIFICATION_CODE_SEND_LIMIT_EXCEEDED,
                            Map.of(
                                    "redisKey",
                                    perMinuteIpVerificationCodeSendRecordRedisKey,
                                    "currentCount",
                                    ipPerMinuteSendCount,
                                    "maxCount",
                                    IP_PER_MINUTE_SEND_COUNT_MAX)));

            CheckUtils.isTrue(
                    ipPerDaySendCount < IP_PER_DAY_SEND_COUNT_MAX,
                    new BusinessException(
                            "今日发送次数已达上限",
                            ErrorType.VERIFICATION_CODE_SEND_LIMIT_EXCEEDED,
                            Map.of(
                                    "redisKey",
                                    perDayIpVerificationCodeSendRecordRedisKey,
                                    "currentCount",
                                    ipPerDaySendCount,
                                    "maxCount",
                                    IP_PER_DAY_SEND_COUNT_MAX)));
            return Future.succeededFuture();
        });
    }

    private String verificationCodeSendRecordRedisKey(
            VerificationCodeType verificationCodeType, String targetType, String target, String periodType) {
        return RedisUtils.generateKey(
                "verification_code_send_record_sorted_set",
                verificationCodeType.name().toLowerCase(),
                targetType,
                target,
                periodType);
    }

    private String verificationCodeRedisKey(
            VerificationCodeType verificationCodeType, NotificationChannelType notificationChannelType, String target) {
        return RedisUtils.generateKey(
                "verification_code_string",
                verificationCodeType.name().toLowerCase(),
                notificationChannelType.name().toLowerCase(),
                target);
    }
}

/*
 * SPDX-FileCopyrightText: 2026 Yu Junyang (https://github.com/lowkeyfish)
 * SPDX-License-Identifier: MIT
 */

package com.yujunyang.vertx.template.common.utils;

import io.vertx.core.Future;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public final class CheckUtils {

    public static <T> void notNull(T object, String errorMessagePattern, Object... errorMessageArgs) {
        notNull(object, new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <T, E extends RuntimeException> void notNull(T object, E exception) throws E {
        if (object == null) {
            throw exception;
        }
    }

    public static <T, E extends Exception> void notNull(T object, E exception) throws E {
        if (object == null) {
            throw exception;
        }
    }

    public static void anyNotNull(Object[] objects, String errorMessagePattern, Object... errorMessageArgs) {
        anyNotNull(objects, new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void anyNotNull(Object[] objects, E exception) throws E {
        if (!ObjectUtils.anyNotNull(objects)) {
            throw exception;
        }
    }

    public static <E extends Exception> void anyNotNull(Object[] objects, E exception) throws E {
        if (!ObjectUtils.anyNotNull(objects)) {
            throw exception;
        }
    }

    public static void notBlank(CharSequence input, String errorMessagePattern, Object... errorMessageArgs) {
        notBlank(input, new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void notBlank(CharSequence input, E exception) {
        if (StringUtils.isBlank(input)) {
            throw exception;
        }
    }

    public static <E extends Exception> void notBlank(CharSequence input, E exception) throws E {
        if (StringUtils.isBlank(input)) {
            throw exception;
        }
    }

    public static void anyNotBlank(String[] strings, String errorMessagePattern, Object... errorMessageArgs) {
        anyNotBlank(strings, new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void anyNotBlank(CharSequence[] strings, E exception) {
        boolean allBlank = true;
        for (CharSequence string : strings) {
            if (StringUtils.isNotBlank(string)) {
                allBlank = false;
                break;
            }
        }
        if (allBlank) {
            throw exception;
        }
    }

    public static <E extends Exception> void anyNotBlank(CharSequence[] strings, E exception) throws E {
        boolean allBlank = true;
        for (CharSequence string : strings) {
            if (StringUtils.isNotBlank(string)) {
                allBlank = false;
                break;
            }
        }
        if (allBlank) {
            throw exception;
        }
    }

    public static void isTrue(boolean expression, String errorMessagePattern, Object... errorMessageArgs) {
        isTrue(expression, new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void isTrue(boolean expression, E exception) {
        if (!expression) {
            throw exception;
        }
    }

    public static <E extends Exception> void isTrue(boolean expression, E exception) throws E {
        if (!expression) {
            throw exception;
        }
    }

    public static <E extends RuntimeException> void isTrue(
            boolean expression, Class<E> exceptionType, String errorMessagePattern, Object... errorMessageArgs) {
        isTrue(expression, createException(exceptionType, errorMessagePattern, errorMessageArgs));
    }

    public static <E extends Exception> void isTrueCheckException(
            boolean expression, Class<E> exceptionType, String errorMessagePattern, Object... errorMessageArgs)
            throws E {
        isTrue(expression, createException(exceptionType, errorMessagePattern, errorMessageArgs));
    }

    public static void moreThan(int number, int compareNumber, String errorMessagePattern, Object... errorMessageArgs) {
        moreThan(
                number,
                compareNumber,
                new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void moreThan(int number, int compareNumber, E exception) {
        if (number <= compareNumber) {
            throw exception;
        }
    }

    public static <E extends Exception> void moreThan(int number, int compareNumber, E exception) throws E {
        if (number <= compareNumber) {
            throw exception;
        }
    }

    public static void moreThan(
            long number, long compareNumber, String errorMessagePattern, Object... errorMessageArgs) {
        moreThan(
                number,
                compareNumber,
                new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void moreThan(long number, long compareNumber, E exception) {
        if (number <= compareNumber) {
            throw exception;
        }
    }

    public static <E extends Exception> void moreThan(long number, long compareNumber, E exception) throws E {
        if (number <= compareNumber) {
            throw exception;
        }
    }

    public static void notEmpty(Collection collection, String errorMessagePattern, Object... errorMessageArgs) {
        notEmpty(collection, new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void notEmpty(Collection collection, E exception) {
        if (collection == null || collection.isEmpty()) {
            throw exception;
        }
    }

    public static <E extends Exception> void notEmpty(Collection collection, E exception) throws Exception {
        if (collection == null || collection.isEmpty()) {
            throw exception;
        }
    }

    public static void notEmpty(Map map, String errorMessagePattern, Object... errorMessageArgs) {
        notEmpty(map, new IllegalArgumentException(MessageFormat.format(errorMessagePattern, errorMessageArgs)));
    }

    public static <E extends RuntimeException> void notEmpty(Map map, E exception) {
        if (map == null || map.isEmpty()) {
            throw exception;
        }
    }

    public static <E extends Exception> void notEmpty(Map map, E exception) throws Exception {
        if (map == null || map.isEmpty()) {
            throw exception;
        }
    }

    private static <E extends Exception> E createException(
            Class<E> exceptionType, String errorMessagePattern, Object... errorMessageArgs) {
        try {
            Constructor<?> constructor = exceptionType.getConstructor(String.class);
            E exception = (E) constructor.newInstance(MessageFormat.format(errorMessagePattern, errorMessageArgs));
            return exception;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Future<Void> future(Runnable runnable) {
        try {
            runnable.run();
            return Future.succeededFuture();
        } catch (Exception e) {
            return Future.failedFuture(e);
        }
    }
}

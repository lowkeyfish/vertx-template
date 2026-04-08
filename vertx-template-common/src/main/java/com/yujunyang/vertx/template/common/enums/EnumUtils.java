package com.yujunyang.vertx.template.common.enums;

import com.google.common.primitives.Ints;
import java.util.LinkedHashMap;
import java.util.Map;

/** 枚举类型帮助类 */
public final class EnumUtils {

    /**
     * 将枚举类型的可选项转换为map
     *
     * @param eClass 枚举类型
     * @param <E> 枚举类型泛型
     * @return 由枚举可选项的value，description组成的map
     */
    public static <E extends Enum & ValueEnum & DescriptionEnum> Map<String, String> toMap(Class<E> eClass) {
        Map<String, String> map = new LinkedHashMap<>();
        for (E e : eClass.getEnumConstants()) {
            map.put(String.valueOf(e.getValue()), e.getDescription());
        }
        return map;
    }

    /**
     * 将枚举类型的可选项转换为map
     *
     * @param eClass 枚举类型
     * @param exclude 排除掉的枚举值
     * @param <E> 枚举类型泛型
     * @return 由枚举可选项的value，description组成的map
     */
    public static <E extends Enum & ValueEnum & DescriptionEnum> Map<String, String> toMap(Class<E> eClass, E exclude) {
        Map<String, String> map = new LinkedHashMap<>();
        for (E e : eClass.getEnumConstants()) {
            if (e != exclude) {
                map.put(String.valueOf(e.getValue()), e.getDescription());
            }
        }
        return map;
    }

    /**
     * 根据枚举value获取枚举
     *
     * @param value 枚举value
     * @param eClass 枚举类型
     * @param <E> 枚举类型泛型
     * @return 枚举
     */
    public static <V, E extends Enum & ValueEnum<V>> E getByValue(V value, Class<E> eClass) {
        for (E e : eClass.getEnumConstants()) {
            if (e.getValue().equals(value)) {
                return e;
            }
        }
        return null;
    }

    public static <V, E extends Enum & ValueEnum<V>> E getByValue(V value, Class<E> eClass, E defaultValue) {
        E ret = getByValue(value, eClass);
        return ret == null ? defaultValue : ret;
    }

    public static <E extends Enum & ValueEnum> E getByName(String name, Class<E> eClass) {
        for (E e : eClass.getEnumConstants()) {
            if (e.name().equals(name)) {
                return e;
            }
        }
        return null;
    }

    public static <E extends Enum & ValueEnum> E getByName(String name, Class<E> eClass, E defaultValue) {
        E ret = getByName(name, eClass);
        return ret == null ? defaultValue : ret;
    }

    public static <E extends Enum & ValueEnum<Integer>> E getByIntValueOrStringName(Object value, Class<E> eClass) {
        if (value instanceof Integer) {
            return getByValue((Integer) value, eClass);
        } else if (value instanceof String) {
            Integer intValue = Ints.tryParse((String) value);
            if (intValue != null) {
                return getByValue(intValue, eClass);
            }
            return getByName((String) value, eClass);
        }
        return null;
    }
}

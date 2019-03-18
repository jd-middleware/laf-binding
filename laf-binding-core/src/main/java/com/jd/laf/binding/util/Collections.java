package com.jd.laf.binding.util;

import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * 集合工具类
 */
public abstract class Collections {

    /**
     * 创建集合对象
     *
     * @param targetType
     * @param size
     * @return
     * @throws Exception
     */
    public static Collection create(final Class<?> targetType, final int size) throws Exception {
        if (targetType == null) {
            return null;
        } else if (targetType.equals(List.class)) {
            return new ArrayList(size);
        } else if (targetType.equals(Set.class)) {
            return new HashSet(size);
        } else if (targetType.equals(SortedSet.class)) {
            return new TreeSet();
        } else if (targetType.isInterface()) {
            // 接口
            return null;
        } else if (Modifier.isAbstract(targetType.getModifiers())) {
            //抽象方法
            return null;
        } else {
            return (Collection) targetType.newInstance();
        }
    }

    /**
     * 根据Key获取值，如果不存在则调用function创建
     *
     * @param map
     * @param key
     * @param function
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> V computeIfAbsent(final ConcurrentMap<K, V> map, final K key, final Function<K, V> function) {
        V result = map.get(key);
        if (result == null) {
            result = function.apply(key);
            V exists = map.putIfAbsent(key, result);
            if (exists != null) {
                result = exists;
            }
        }
        return result;
    }
}

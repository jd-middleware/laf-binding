package com.jd.laf.binding.util;

/**
 * 转换
 *
 * @param <K>
 * @param <V>
 */
public interface Function<K, V> {

    V apply(K key);
}

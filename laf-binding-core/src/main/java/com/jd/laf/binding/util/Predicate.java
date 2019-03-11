package com.jd.laf.binding.util;

/**
 * 测试
 *
 * @param <T>
 */
public interface Predicate<T> {

    /**
     * 检测是否通过
     *
     * @param obj
     * @return
     */
    boolean test(T obj);
}

package com.jd.laf.binding.reflect;

import com.jd.laf.binding.reflect.exception.ReflectionException;

/**
 * 访问器
 */
public interface FieldAccessor {

    /**
     * 获取值
     *
     * @param target 目标对象
     * @return 属性值
     * @throws ReflectionException
     */
    Object get(final Object target) throws ReflectionException;

    /**
     * 设置值
     *
     * @param target 目标对象
     * @param value  属性值
     * @throws ReflectionException
     */
    void set(final Object target, final Object value) throws ReflectionException;
}

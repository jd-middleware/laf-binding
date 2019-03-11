package com.jd.laf.binding.reflect;

import java.lang.annotation.Annotation;

/**
 * 方法参数
 */
public interface MethodParameter {

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 返回序号
     *
     * @return
     */
    int getIndex();

    /**
     * 参数类型
     *
     * @return
     */
    Class<?> getType();

    /**
     * 获取参数化类型
     *
     * @return
     */
    Class<?> getGenericType();

    /**
     * 获取注解
     *
     * @return
     */
    Annotation[] getAnnotations();

    /**
     * 返回目标对象
     *
     * @return
     */
    Object target();
}

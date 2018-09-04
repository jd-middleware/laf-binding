package com.jd.laf.binding.reflect;

/**
 * 属性获取器
 */
public interface PropertySupplier {

    /**
     * 获取属性值
     *
     * @param target 对象
     * @param name   属性
     * @return 属性值
     * @throws Exception
     */
    Object get(Object target, String name) throws Exception;

    /**
     * 是否支持该类型的属性值获取
     *
     * @param clazz 类型
     * @return 支持标识
     */
    boolean support(Class<?> clazz);
}

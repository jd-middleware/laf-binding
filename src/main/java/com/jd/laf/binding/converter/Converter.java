package com.jd.laf.binding.converter;

/**
 * 自定义简单类型转换，不支持集合和数组，字符串和数组，字符串和集合直接的转换
 */
public interface Converter {

    /**
     * 类型转换
     *
     * @param conversion 转换请求
     * @return
     */
    Object convert(Conversion conversion);

    /**
     * 是否支持该类型转换
     *
     * @param type
     * @return
     */
    boolean support(Class<?> type);

    /**
     * 支持的目标类型，基本类型会自动装箱
     *
     * @return 字段类型
     */
    Class<?> type();
}

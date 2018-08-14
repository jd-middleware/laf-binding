package com.jd.laf.binding.converter;

/**
 * 自定义类型转换
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

package com.jd.laf.binding.converter;

/**
 * 简单类型转换，支持固定的简单目标类型转换
 */
public interface SimpleConverter extends Converter {

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

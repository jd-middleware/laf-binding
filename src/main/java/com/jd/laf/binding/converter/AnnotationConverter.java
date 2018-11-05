package com.jd.laf.binding.converter;

/**
 * 注解转换器
 */
public interface AnnotationConverter extends Converter {

    /**
     * 是否支持该类型转换
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return
     */
    boolean support(Class<?> sourceType, Class<?> targetType);

    /**
     * 支持的绑定注解类型
     *
     * @return 绑定注解类型
     */
    Class<?> annotation();

}

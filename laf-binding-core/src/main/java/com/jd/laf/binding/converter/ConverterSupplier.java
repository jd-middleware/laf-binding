package com.jd.laf.binding.converter;

import com.jd.laf.extension.Ordered;

/**
 * 转换提供者
 */
public interface ConverterSupplier extends Ordered {

    /**
     * 转换器提供者
     */
    int TRANSFORMER_SUPPLIER_ORDER = 0;

    /**
     * 简单类型提供者
     */
    int SIMPLE_SUPPLIER_ORDER = 1;

    /**
     * 构造函数提供者
     */
    int CONSTRUCTOR_SUPPLIER_ORDER = 2;

    /**
     * valueOf静态方法提供者
     */
    int VALUE_OF_SUPPLIER_ORDER = 10;

    /**
     * fromString静态方法提供者
     */
    int FROM_STRING_SUPPLIER_ORDER = 10;

    /**
     * MAP到对象
     */
    int MAP_TO_OBJECT_ORDER = 50;

    /**
     * 字符串到数组
     */
    int STRING_TO_ARRAY_ORDER = 99;

    /**
     * 字符串到集合
     */
    int STRING_TO_COLLECTION_ORDER = 99;

    /**
     * 数组到数组
     */
    int ARRAY_TO_ARRAY_ORDER = 99;

    /**
     * 数组到集合
     */
    int ARRAY_TO_COLLECTION_ORDER = 99;

    /**
     * 集合到数组
     */
    int COLLECTION_TO_ARRAY_ORDER = 99;

    /**
     * 集合到集合
     */
    int COLLECTION_TO_COLLECTION_ORDER = 99;

    /**
     * 获取转换操作
     *
     * @param type 转换类型
     * @return 转换操作
     */
    Converter getConverter(ConversionType type);

}

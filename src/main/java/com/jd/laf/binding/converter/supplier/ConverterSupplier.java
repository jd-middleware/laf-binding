package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.Ordered;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;

/**
 * 转换提供者
 */
public interface ConverterSupplier extends Ordered {

    int ANNOTATION_SUPPLIER_ORDER = 0;

    int SIMPLE_SUPPLIER_ORDER = 1;

    int CONSTRUCTOR_SUPPLIER_ORDER = 2;

    int VALUE_OF_SUPPLIER_ORDER = 3;

    int FROM_STRING_SUPPLIER_ORDER = 4;

    int STRING_TO_ARRAY_ORDER = 99;

    int STRING_TO_COLLECTION_ORDER = 99;

    int ARRAY_TO_ARRAY_ORDER = 99;

    int ARRAY_TO_COLLECTION_ORDER = 99;

    int COLLECTION_TO_ARRAY_ORDER = 99;

    int COLLECTION_TO_COLLECTION_ORDER = 99;

    int MAP_TO_OBJECT_ORDER = 50;

    /**
     * 获取转换操作
     *
     * @param type 转换类型
     * @return 转换操作
     */
    Converter getConverter(ConversionType type);

}

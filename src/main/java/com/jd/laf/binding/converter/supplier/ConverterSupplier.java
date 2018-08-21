package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.converter.Conversion;

/**
 * 转换提供者
 */
public interface ConverterSupplier {

    int CUSTOM_SUPPLIER_ORDER = 0;

    int CONSTRUCTOR_SUPPLIER_ORDER = 1;

    int VALUE_OF_SUPPLIER_ORDER = 2;

    int FROM_STRING_SUPPLIER_ORDER = 2;

    int STRING_TO_ARRAY_ORDER = 99;

    int STRING_TO_COLLECTION_ORDER = 99;

    int ARRAY_TO_ARRAY_ORDER = 99;

    int ARRAY_TO_COLLECTION_ORDER = 99;

    int COLLECTION_TO_ARRAY_ORDER = 99;

    int COLLECTION_TO_COLLECTION_ORDER = 99;

    /**
     * 获取转换操作
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 转换操作
     */
    Operation getOperation(Class<?> sourceType, Class<?> targetType);

    /**
     * 优先级顺序，数值越低，优先级越高
     *
     * @return
     */
    int order();


    /**
     * 转换操作
     */
    interface Operation {

        /**
         * 执行
         *
         * @param conversion 转换对象
         * @return 转换后的对象
         * @throws Exception
         */
        Object execute(Conversion conversion) throws Exception;

    }

}

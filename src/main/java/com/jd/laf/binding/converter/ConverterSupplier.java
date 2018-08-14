package com.jd.laf.binding.converter;

/**
 * 转换提供者
 */
public interface ConverterSupplier {

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

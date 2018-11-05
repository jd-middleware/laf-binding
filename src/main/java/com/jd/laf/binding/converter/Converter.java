package com.jd.laf.binding.converter;

/**
 * 转换操作
 */
public interface Converter {

    /**
     * 执行
     *
     * @param conversion 转换对象
     * @return 转换后的对象
     * @throws Exception
     */
    Object execute(Conversion conversion) throws Exception;
}

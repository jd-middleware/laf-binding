package com.jd.laf.binding.reflect;

import java.lang.reflect.Field;

/**
 * 字段访问器工程
 */
public interface FieldAccessorFactory {

    /**
     * 获取字段访问器
     *
     * @param field 字段
     * @return
     */
    FieldAccessor getAccessor(Field field);

}

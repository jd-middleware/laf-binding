package com.jd.laf.binding.reflect;

import com.jd.laf.extension.Type;

import java.lang.reflect.Field;

/**
 * 字段访问器工程
 */
public interface FieldAccessorFactory extends Type<String> {

    /**
     * 获取字段访问器
     *
     * @param field 字段
     * @return
     */
    FieldAccessor getAccessor(Field field);

}

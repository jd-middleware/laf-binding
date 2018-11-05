package com.jd.laf.binding.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型工具
 */
public abstract class Generics {

    /**
     * 获取泛型
     *
     * @param type
     * @return
     */
    public static Class getGenericType(final Type type) {
        if (type == null) {
            return null;
        }
        // 如果是泛型参数的类型
        if (type instanceof ParameterizedType) {
            //得到泛型里的class类型对象
            Type actualType = ((ParameterizedType) type).getActualTypeArguments()[0];
            if (actualType instanceof Class) {
                return (Class) actualType;
            }
        }
        return null;
    }

}

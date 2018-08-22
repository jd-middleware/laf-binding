package com.jd.laf.binding.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 泛型工具
 */
public abstract class Generics {

    /**
     * 获取泛型
     *
     * @param field
     * @return
     */
    public static Class getGenericType(final Field field) {
        if (field == null) {
            return null;
        }
        Type type = field.getGenericType();
        if (type == null) {
            return null;
        }
        // 如果是泛型参数的类型
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            //得到泛型里的class类型对象
            type = pt.getActualTypeArguments()[0];
            if (type instanceof Class) {
                return (Class) type;
            }
        }
        return null;
    }

    /**
     * 获取泛型
     *
     * @param field
     * @return
     */
    public static List<Class<?>> getGenericTypes(final Field field) {
        if (field == null) {
            return null;
        }
        Type type = field.getGenericType();
        if (type == null) {
            return null;
        }
        // 如果是泛型参数的类型
        if (type instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) type;
            //得到泛型里的class类型对象
            Type[] arguments = pt.getActualTypeArguments();
            List<Class<?>> result = new ArrayList<Class<?>>(arguments.length);
            for (Type argument : arguments) {
                if (argument instanceof Class) {
                    result.add((Class) argument);
                }
            }
            return result;
        }
        return null;
    }
}

package com.jd.laf.binding.reflect;

import com.jd.laf.binding.util.Function;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.util.Collections.computeIfAbsent;

/**
 * 泛型工具
 */
public abstract class Generics {

    protected static final ConcurrentMap<Class, GenericClass> classGeneric = new ConcurrentHashMap<Class, GenericClass>();


    /**
     * 获取泛型信息
     *
     * @param clazz
     * @return
     */
    public static GenericClass get(final Class clazz) {
        if (clazz == null) {
            return null;
        }
        return computeIfAbsent(classGeneric, clazz, new Function<Class, GenericClass>() {
            @Override
            public GenericClass apply(Class key) {
                return new GenericClass(clazz);
            }
        });
    }

}

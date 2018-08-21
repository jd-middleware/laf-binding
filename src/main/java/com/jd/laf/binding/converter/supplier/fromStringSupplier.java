package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.Option;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 根据静态的fromString方法进行转换
 */
public class fromStringSupplier extends StaticMethodSupplier {

    //单参数构造函数映射
    protected static ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Method>>> methods =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<Class<?>, Option<Method>>>();

    public static final String FROM_STRING = "fromString";

    public fromStringSupplier() {
        super(FROM_STRING);
    }

    @Override
    public int order() {
        return FROM_STRING_SUPPLIER_ORDER;
    }

    @Override
    protected ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Method>>> getCache() {
        return methods;
    }
}

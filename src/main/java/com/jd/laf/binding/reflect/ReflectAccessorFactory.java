package com.jd.laf.binding.reflect;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 字段访问器工厂
 */
public class ReflectAccessorFactory implements FieldAccessorFactory {

    protected static final FieldAccessorFactory INSTANCE = new ReflectAccessorFactory();

    protected static ConcurrentMap<Field, FieldAccessor> accessors = new ConcurrentHashMap<Field, FieldAccessor>();

    public static FieldAccessorFactory getInstance() {
        return INSTANCE;
    }

    @Override
    public FieldAccessor getAccessor(final Field field) {
        if (field == null) {
            return null;
        }
        FieldAccessor accessor = accessors.get(field);
        if (accessor == null) {
            accessor = new ReflectAccessor(field);
            FieldAccessor exists = accessors.putIfAbsent(field, accessor);
            if (exists != null) {
                accessor = exists;
            }
        }
        return accessor;
    }
}

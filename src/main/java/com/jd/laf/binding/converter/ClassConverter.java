package com.jd.laf.binding.converter;

import com.jd.laf.binding.reflect.Classes;

/**
 * 类转换器
 */
public class ClassConverter implements SimpleConverter {

    @Override
    public Object execute(final Conversion conversion) {
        if (conversion == null) {
            return null;
        }
        Object value = conversion.source;
        if (value != null && value instanceof CharSequence) {
            return Classes.getClass(value.toString());
        }
        return null;
    }

    @Override
    public boolean support(Class<?> type) {
        return type != null && CharSequence.class.isAssignableFrom(type);
    }

    @Override
    public Class<?> type() {
        return Class.class;
    }
}

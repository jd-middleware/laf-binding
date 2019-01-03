package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;
import com.jd.laf.binding.reflect.Classes;

/**
 * 类转换器
 */
public class ClassConverter implements SimpleConverter {

    @Override
    public Class execute(final Conversion conversion) {
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
    public boolean support(Class<?> sourceType) {
        return sourceType != null && CharSequence.class.isAssignableFrom(sourceType);
    }

    @Override
    public Class<?> targetType() {
        return Class.class;
    }
}

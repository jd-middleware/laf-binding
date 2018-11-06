package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.SimpleConverter;

/**
 * 数值转换器
 */
public abstract class NumberConverter implements SimpleConverter {

    @Override
    public boolean support(final Class<?> sourceType) {
        if (sourceType == null) {
            return false;
        } else if (Number.class.isAssignableFrom(sourceType)) {
            return true;
        } else if (Character.class.isAssignableFrom(sourceType)) {
            return true;
        } else if (CharSequence.class.isAssignableFrom(sourceType)) {
            return true;
        }
        return false;
    }
}

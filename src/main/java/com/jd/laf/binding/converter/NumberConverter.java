package com.jd.laf.binding.converter;

/**
 * 数值转换器
 */
public abstract class NumberConverter implements SimpleConverter {

    @Override
    public boolean support(final Class<?> type) {
        if (type == null) {
            return false;
        } else if (Number.class.isAssignableFrom(type)) {
            return true;
        } else if (Character.class.isAssignableFrom(type)) {
            return true;
        } else if (CharSequence.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }
}

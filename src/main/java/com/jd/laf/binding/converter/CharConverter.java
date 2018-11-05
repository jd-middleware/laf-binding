package com.jd.laf.binding.converter;

/**
 * 字符转换器
 */
public class CharConverter implements SimpleConverter {

    @Override
    public Object execute(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        }
        if (conversion.source instanceof CharSequence) {
            return conversion.source.toString().trim().charAt(0);
        }
        return null;
    }

    @Override
    public boolean support(final Class<?> type) {
        if (type == null) {
            return false;
        } else if (CharSequence.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    @Override
    public Class<?> type() {
        return Character.class;
    }
}

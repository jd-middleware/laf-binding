package com.jd.laf.binding.converter;

/**
 * 字符转换器
 */
public class CharConverter implements Converter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        }
        if (conversion.source instanceof CharSequence) {
            return conversion.source.toString().charAt(0);
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
    public Class<?>[] types() {
        return new Class[]{char.class, Character.class};
    }
}

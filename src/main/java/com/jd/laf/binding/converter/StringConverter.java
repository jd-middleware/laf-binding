package com.jd.laf.binding.converter;

/**
 * 字符串转换器
 */
public class StringConverter implements Converter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        }
        return conversion.source.toString();
    }

    @Override
    public boolean support(Class<?> type) {
        return true;
    }

    @Override
    public Class<?> type() {
        return String.class;
    }
}

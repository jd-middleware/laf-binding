package com.jd.laf.binding.converter;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL转换器
 */
public class URLConverter implements Converter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null) {
            return null;
        }
        Object value = conversion.source;
        if (value != null && value instanceof CharSequence) {
            try {
                return new URL(value.toString());
            } catch (MalformedURLException e) {
            }
        }
        return null;
    }

    @Override
    public boolean support(Class<?> type) {
        return type != null && CharSequence.class.isAssignableFrom(type);
    }

    @Override
    public Class<?> type() {
        return URL.class;
    }
}

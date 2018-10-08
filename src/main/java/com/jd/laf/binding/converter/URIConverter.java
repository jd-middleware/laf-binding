package com.jd.laf.binding.converter;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * URI转换器
 */
public class URIConverter implements Converter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null) {
            return null;
        }
        Object value = conversion.source;
        if (value != null && value instanceof CharSequence) {
            try {
                return new URI(value.toString());
            } catch (URISyntaxException e) {
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
        return URI.class;
    }
}

package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL转换器
 */
public class URLConverter implements SimpleConverter {

    @Override
    public URL execute(final Conversion conversion) {
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
    public boolean support(Class<?> sourceType) {
        return sourceType != null && CharSequence.class.isAssignableFrom(sourceType);
    }

    @Override
    public Class<?> targetType() {
        return URL.class;
    }
}

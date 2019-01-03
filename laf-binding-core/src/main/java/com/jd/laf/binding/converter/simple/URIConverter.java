package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * URI转换器
 */
public class URIConverter implements SimpleConverter {

    @Override
    public URI execute(final Conversion conversion) {
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
    public boolean support(Class<?> sourceType) {
        return sourceType != null && CharSequence.class.isAssignableFrom(sourceType);
    }

    @Override
    public Class<?> targetType() {
        return URI.class;
    }
}

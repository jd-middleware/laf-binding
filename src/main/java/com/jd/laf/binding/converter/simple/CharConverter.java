package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;

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
    public boolean support(final Class<?> sourceType) {
        if (sourceType == null) {
            return false;
        } else if (CharSequence.class.isAssignableFrom(sourceType)) {
            return true;
        }
        return false;
    }

    @Override
    public Class<?> targetType() {
        return Character.class;
    }
}

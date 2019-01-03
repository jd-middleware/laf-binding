package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 布尔转换器
 */
public class AtomicBooleanConverter extends NumberConverter {

    @Override
    public AtomicBoolean execute(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return new AtomicBoolean(((Number) conversion.source).intValue() != 0 ? Boolean.TRUE : Boolean.FALSE);
        } else if (conversion.source instanceof Character) {
            return new AtomicBoolean(((Character) conversion.source) != '0' ? Boolean.TRUE : Boolean.FALSE);
        } else if (conversion.source instanceof CharSequence) {
            String value = conversion.source.toString().trim();
            if ("true".equalsIgnoreCase(value)) {
                return new AtomicBoolean(true);
            } else if ("false".equalsIgnoreCase(value)) {
                return new AtomicBoolean(false);
            }
            try {
                return new AtomicBoolean(Long.parseLong(value) != 0);
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public Class<?> targetType() {
        return Boolean.class;
    }
}

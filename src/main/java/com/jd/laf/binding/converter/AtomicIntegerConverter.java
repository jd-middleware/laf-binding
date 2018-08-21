package com.jd.laf.binding.converter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 长整数转换器
 */
public class AtomicIntegerConverter extends NumberConverter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return new AtomicInteger(((Number) conversion.source).intValue());
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return new AtomicInteger(Integer.parseInt((conversion.source.toString().trim())));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?> type() {
        return Long.class;
    }
}

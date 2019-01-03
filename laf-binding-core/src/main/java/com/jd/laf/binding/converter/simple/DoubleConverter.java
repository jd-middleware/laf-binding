package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;

/**
 * 双精度浮点数转换器
 */
public class DoubleConverter extends NumberConverter {

    @Override
    public Double execute(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return ((Number) conversion.source).doubleValue();
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return Double.parseDouble((conversion.source.toString().trim()));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?> targetType() {
        return Double.class;
    }
}

package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;

/**
 * 浮点数转换器
 */
public class FloatConverter extends NumberConverter {

    @Override
    public Float execute(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return ((Number) conversion.source).floatValue();
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return Float.parseFloat((conversion.source.toString().trim()));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?> targetType() {
        return Float.class;
    }
}

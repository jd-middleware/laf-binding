package com.jd.laf.binding.converter;

/**
 * 浮点数转换器
 */
public class FloatConverter extends NumberConverter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return ((Number) conversion.source).floatValue();
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return Float.parseFloat((conversion.source.toString()));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?> type() {
        return Float.class;
    }
}

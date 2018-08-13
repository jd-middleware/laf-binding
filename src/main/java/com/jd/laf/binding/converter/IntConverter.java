package com.jd.laf.binding.converter;

/**
 * 整数转换器
 */
public class IntConverter extends NumberConverter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return ((Number) conversion.source).intValue();
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return Integer.parseInt((conversion.source.toString()));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?>[] types() {
        return new Class[]{int.class, Integer.class};
    }
}

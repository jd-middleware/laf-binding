package com.jd.laf.binding.converter;

/**
 * 短整数转换器
 */
public class ShortConverter extends NumberConverter {

    @Override
    public Object execute(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return ((Number) conversion.source).shortValue();
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return Short.parseShort((conversion.source.toString().trim()));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?> type() {
        return Short.class;
    }
}

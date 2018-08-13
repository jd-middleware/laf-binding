package com.jd.laf.binding.converter;

/**
 * 字节转换器
 */
public class ByteConverter extends NumberConverter {

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return ((Number) conversion.source).byteValue();
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return Byte.parseByte((conversion.source.toString()));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?>[] types() {
        return new Class[]{byte.class, Byte.class};
    }
}

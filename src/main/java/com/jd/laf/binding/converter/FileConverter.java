package com.jd.laf.binding.converter;

import java.io.File;

/**
 * 文件转换器
 */
public class FileConverter implements SimpleConverter {

    @Override
    public Object execute(final Conversion conversion) {
        if (conversion == null) {
            return null;
        }
        Object value = conversion.source;
        if (value != null && value instanceof CharSequence) {
            return new File(value.toString());
        }
        return null;
    }

    @Override
    public boolean support(Class<?> type) {
        return type != null && CharSequence.class.isAssignableFrom(type);
    }

    @Override
    public Class<?> type() {
        return File.class;
    }
}

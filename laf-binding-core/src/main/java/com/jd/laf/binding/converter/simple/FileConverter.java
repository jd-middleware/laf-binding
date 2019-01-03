package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;

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
    public boolean support(Class<?> sourceType) {
        return sourceType != null && CharSequence.class.isAssignableFrom(sourceType);
    }

    @Override
    public Class<?> targetType() {
        return File.class;
    }
}

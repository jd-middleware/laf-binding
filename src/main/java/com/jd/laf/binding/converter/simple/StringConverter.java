package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;

import java.lang.reflect.Array;
import java.util.Collection;

/**
 * 字符串转换器
 */
public class StringConverter implements SimpleConverter {

    @Override
    public Object execute(final Conversion conversion) {
        if (conversion == null) {
            return null;
        }
        Object value = conversion.source;
        if (value == null) {
            return null;
        } else if (value instanceof CharSequence) {
            return value.toString();
        } else if (value instanceof Collection) {
            //集合类型，以逗号分隔
            StringBuilder builder = new StringBuilder();
            Collection<?> collection = (Collection<?>) value;
            int count = 0;
            for (Object item : collection) {
                if (count++ > 0) {
                    builder.append(',');
                }
                if (item != null) {
                    builder.append(item.toString());
                }
            }
            return builder.toString();
        } else if (value.getClass().isArray()) {
            //数组类型，以逗号分隔
            int length = Array.getLength(value);
            StringBuilder builder = new StringBuilder();
            Object item;
            for (int i = 0; i < length; i++) {
                if (i > 0) {
                    builder.append(',');
                }
                item = Array.get(value, i);
                if (item != null) {
                    builder.append(item.toString());
                }
            }
            return builder.toString();
        }
        return value.toString();
    }

    @Override
    public boolean support(Class<?> sourceType) {
        return true;
    }

    @Override
    public Class<?> targetType() {
        return String.class;
    }
}

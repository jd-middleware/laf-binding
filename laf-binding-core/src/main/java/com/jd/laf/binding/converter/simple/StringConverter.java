package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;
import com.jd.laf.binding.util.Strings;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Properties;

/**
 * 字符串转换器
 */
public class StringConverter implements SimpleConverter {

    @Override
    public String execute(final Conversion conversion) {
        if (conversion == null) {
            return null;
        }
        Object value = conversion.source;
        if (value == null) {
            return null;
        } else if (value instanceof CharSequence) {
            return value.toString();
        } else if (value instanceof Properties) {
            return onProperties((Properties) value);
        } else if (value instanceof Collection) {
            return onCollection((Collection<?>) value);
        } else if (value.getClass().isArray()) {
            return onArray(value);
        }
        return value.toString();
    }

    /**
     * Properties处理
     *
     * @param value
     * @return
     */
    protected String onProperties(Properties value) {
        return Strings.toString(value);
    }

    /**
     * 数组
     *
     * @param value
     * @return
     */
    protected String onArray(final Object value) {
        //数组类型，以逗号分隔
        int length = Array.getLength(value);
        StringBuilder builder = new StringBuilder(100);
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

    /**
     * 集合
     *
     * @param value
     * @return
     */
    protected String onCollection(final Collection<?> value) {
        //集合类型，以逗号分隔
        StringBuilder builder = new StringBuilder(100);
        Collection<?> collection = value;
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

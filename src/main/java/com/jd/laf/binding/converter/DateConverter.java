package com.jd.laf.binding.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间转换器
 */
public class DateConverter implements Converter {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Calendar) {
            return ((Calendar) conversion.source).getTime();
        } else if (conversion.source instanceof Number) {
            return new Date(((Number) conversion.source).longValue());
        } else if (conversion.source instanceof CharSequence) {
            String source = ((CharSequence) conversion.source).toString();
            String format = conversion.format == null ? null : conversion.format.toString();
            if (format == null || format.isEmpty()) {
                switch (source.length()) {
                    case 19:
                        format = YYYY_MM_DD_HH_MM_SS;
                        break;
                    case 10:
                        format = YYYY_MM_DD;
                        break;
                    case 8:
                        format = HH_MM_SS;
                        break;
                    default:
                        format = null;
                }
            }
            if (format != null) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat(format);
                    return sdf.parse(source);
                } catch (ParseException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @Override
    public boolean support(final Class<?> type) {
        if (type == null) {
            return false;
        } else if (Calendar.class.isAssignableFrom(type)) {
            return true;
        } else if (Number.class.isAssignableFrom(type)) {
            return true;
        } else if (CharSequence.class.isAssignableFrom(type)) {
            return true;
        }
        return false;
    }

    @Override
    public Class<?>[] types() {
        return new Class[]{Date.class};
    }
}

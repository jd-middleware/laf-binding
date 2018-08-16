package com.jd.laf.binding.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间转换器
 */
public class DateConverter implements Converter {

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String HH_MM_SS = "HH:mm:ss";

    //DateFormat不是线程安全的
    protected static ThreadLocal<Map<String, DateFormat>> threadLocal = new ThreadLocal<Map<String, DateFormat>>() {
        @Override
        protected Map<String, DateFormat> initialValue() {
            return new HashMap<String, DateFormat>();
        }
    };

    /**
     * 获取日期格式化
     *
     * @param format
     * @return
     */
    protected static DateFormat getDateFormat(String format) {
        if (format == null || format.isEmpty()) {
            return null;
        }

        Map<String, DateFormat> formats = threadLocal.get();
        DateFormat result = formats.get(format);
        if (result == null) {
            result = new SimpleDateFormat(format);
            formats.put(format, result);
        }
        return result;
    }

    @Override
    public Object convert(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Calendar) {
            return ((Calendar) conversion.source).getTime();
        } else if (conversion.source instanceof Number) {
            return new Date(((Number) conversion.source).longValue());
        } else if (conversion.source instanceof CharSequence) {
            String source = ((CharSequence) conversion.source).toString().trim();
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
                    DateFormat sdf = getDateFormat(format);
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
    public Class<?> type() {
        return Date.class;
    }
}

package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

public abstract class DateTimeConverter<T> implements SimpleConverter {

    public static final DateTimeFormatter YYYY_MM_DD_HH_MM_SS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public T execute(final Conversion conversion) {
        try {
            if (conversion == null || conversion.source == null) {
                return null;
            } else if (conversion.source instanceof Calendar) {
                return convert(((Calendar) conversion.source).toInstant());
            } else if (conversion.source instanceof Date) {
                return convert(((Date) conversion.source).toInstant());
            } else if (conversion.source instanceof Number) {
                return convert((Number) conversion.source);
            } else if (conversion.source instanceof TemporalAccessor) {
                return convert((TemporalAccessor) conversion.source);
            } else if (conversion.source instanceof CharSequence) {
                String source = conversion.source.toString().trim();
                String format = conversion.format == null ? null : conversion.format.toString().trim();
                //配置了格式化字符串
                if (format != null && !format.isEmpty()) {
                    return convert(DateTimeFormatter.ofPattern(format).parse(source));
                }
                //遍历字符串，猜测格式                }
                return guess(source);

            } else if (conversion.source instanceof Character) {
                return convert(Long.parseLong(((Character) conversion.source).toString()));
            }
        } catch (NumberFormatException e) {
        } catch (DateTimeParseException e) {
        } catch (DateTimeException e) {
        }
        return null;
    }

    protected T guess(final String source) {
        int slash = 0;
        int length = source.length();
        for (int i = 0; i < length; i++) {
            switch (source.charAt(i)) {
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    break;
                case '-':
                    if (slash++ >= 2) {
                        //多余2个横线
                        return null;
                    }
                    break;
                case ' ':
                    //前面2个横线
                    return slash == 2 ? convert(YYYY_MM_DD_HH_MM_SS.parse(source, LocalDateTime::from)) : null;
                case 'T':
                    //前面2个横线
                    return slash == 2 ? convert(ISO_DATE_TIME.parse(source, LocalDateTime::from)) : null;
                default:
                    return null;
            }
        }
        return slash == 2 ? convert(ISO_DATE.parse(source, LocalDate::from)) : (slash == 0 ? convert(Long.parseLong(source)) : null);
    }

    protected T convert(final Number number) {
        return convert(Instant.ofEpochMilli(number.longValue()));
    }

    protected abstract T convert(TemporalAccessor temporal);

    @Override
    public boolean support(final Class<?> sourceType) {
        if (sourceType == null) {
            return false;
        } else if (Calendar.class.isAssignableFrom(sourceType)) {
            return true;
        } else if (Date.class.isAssignableFrom(sourceType)) {
            return true;
        } else if (Number.class.isAssignableFrom(sourceType)) {
            return true;
        } else if (Character.class.isAssignableFrom(sourceType)) {
            return true;
        } else if (CharSequence.class.isAssignableFrom(sourceType)) {
            return true;
        }
        return false;
    }
}
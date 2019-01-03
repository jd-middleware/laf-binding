package com.jd.laf.binding.converter.simple;

import com.jd.laf.extension.Ordered;

import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;

public class Date8Converter extends DateTimeConverter<Date> implements Ordered {

    @Override
    protected Date convert(TemporalAccessor temporal) {
        if (temporal instanceof Instant) {
            return Date.from((Instant) temporal);
        } else if (temporal.isSupported(EPOCH_DAY)) {
            long value = temporal.getLong(EPOCH_DAY) * 24 * 3600 * 1000L;
            if (temporal.isSupported(MILLI_OF_DAY)) {
                value += temporal.getLong(MILLI_OF_DAY);
            }
            return new Date(value);
        }
        return new Date(Instant.from(temporal).toEpochMilli());
    }

    @Override
    public Class<?> targetType() {
        return Date.class;
    }

    @Override
    public int order() {
        //排在java6的日期转换之前
        return Ordered.ORDER - 1;
    }
}
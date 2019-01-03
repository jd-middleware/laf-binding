package com.jd.laf.binding.converter.simple;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;

import static java.time.temporal.ChronoField.EPOCH_DAY;
import static java.time.temporal.ChronoField.MILLI_OF_DAY;

public class InstantConverter extends DateTimeConverter<Instant> {

    @Override
    protected Instant convert(final TemporalAccessor temporal) {
        if (temporal instanceof Instant) {
            return (Instant) temporal;
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).toInstant();
        } else if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).toInstant();
        } else if (temporal.isSupported(EPOCH_DAY)) {
            long value = temporal.getLong(EPOCH_DAY) * 24 * 3600 * 1000L;
            if (temporal.isSupported(MILLI_OF_DAY)) {
                value += temporal.getLong(MILLI_OF_DAY);
            }
            return Instant.ofEpochMilli(value);
        }
        return Instant.from(temporal);
    }

    @Override
    public Class<?> targetType() {
        return Instant.class;
    }
}
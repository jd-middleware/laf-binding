package com.jd.laf.binding.converter.simple;

import java.time.*;
import java.time.temporal.TemporalAccessor;

import static java.time.temporal.ChronoField.EPOCH_DAY;

public class LocalDateConverter extends DateTimeConverter<LocalDate> {

    @Override
    protected LocalDate convert(final TemporalAccessor temporal) {
        if (temporal instanceof LocalDate) {
            return (LocalDate) temporal;
        } else if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).toLocalDate();
        } else if (temporal instanceof Instant) {
            return ((Instant) temporal).atZone(ZoneId.systemDefault()).toLocalDate();
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).toLocalDate();
        } else if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).toLocalDate();
        } else if (temporal.isSupported(EPOCH_DAY)) {
            return LocalDate.ofEpochDay(temporal.getLong(EPOCH_DAY));
        }
        return LocalDate.from(temporal);
    }

    @Override
    public Class<?> targetType() {
        return LocalDate.class;
    }
}
package com.jd.laf.binding.converter.simple;

import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

import static java.time.format.DateTimeFormatter.ISO_TIME;
import static java.time.temporal.ChronoField.NANO_OF_DAY;

public class LocalTimeConverter extends DateTimeConverter<LocalTime> {

    @Override
    protected LocalTime guess(final String source) {
        LocalTime result = super.guess(source);
        return result != null ? result : convert(ISO_TIME.parse(source));
    }

    @Override
    protected LocalTime convert(final Number number) {
        return LocalTime.ofNanoOfDay(number.longValue());
    }

    @Override
    protected LocalTime convert(final TemporalAccessor temporal) {
        if (temporal instanceof LocalTime) {
            return (LocalTime) temporal;
        } else if (temporal instanceof LocalDateTime) {
            return ((LocalDateTime) temporal).toLocalTime();
        } else if (temporal instanceof Instant) {
            return ((Instant) temporal).atZone(ZoneId.systemDefault()).toLocalTime();
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).toLocalTime();
        } else if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).toLocalTime();
        } else if (temporal.isSupported(NANO_OF_DAY)) {
            return LocalTime.ofNanoOfDay(temporal.getLong(NANO_OF_DAY));
        } else {
            LocalTime time = temporal.query(TemporalQueries.localTime());
            return time != null ? time : LocalTime.ofNanoOfDay(0);
        }
    }

    @Override
    public Class<?> targetType() {
        return LocalTime.class;
    }
}
package com.jd.laf.binding.converter.simple;

import java.time.*;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalQueries;

import static java.time.temporal.ChronoField.*;

public class LocalDateTimeConverter extends DateTimeConverter<LocalDateTime> {

    @Override
    protected LocalDateTime convert(final TemporalAccessor temporal) {
        if (temporal instanceof LocalDateTime) {
            return (LocalDateTime) temporal;
        } else if (temporal instanceof Instant) {
            return LocalDateTime.ofInstant((Instant) temporal, ZoneId.systemDefault());
        } else if (temporal instanceof ZonedDateTime) {
            return ((ZonedDateTime) temporal).toLocalDateTime();
        } else if (temporal instanceof OffsetDateTime) {
            return ((OffsetDateTime) temporal).toLocalDateTime();
        } else if (temporal.isSupported(EPOCH_DAY)) {
            long second = temporal.getLong(EPOCH_DAY) * 24 * 3600;
            if (temporal.isSupported(SECOND_OF_DAY)) {
                second += temporal.getLong(SECOND_OF_DAY);
            }
            int nano = temporal.isSupported(NANO_OF_SECOND) ? temporal.get(NANO_OF_SECOND) : 0;
            ZoneOffset offset = temporal.query(TemporalQueries.offset());
            if (offset == null) {
                offset = OffsetDateTime.now().getOffset();
            }
            return LocalDateTime.ofEpochSecond(second, nano, offset);
        }
        return LocalDateTime.from(temporal);
    }

    @Override
    public Class<?> targetType() {
        return LocalDateTime.class;
    }
}
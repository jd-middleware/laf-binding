package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;

import java.time.Duration;

public class DurationConverter extends NumberConverter {

    @Override
    public Duration execute(final Conversion conversion) {
        if (conversion == null || conversion.source == null) {
            return null;
        } else if (conversion.source instanceof Number) {
            return Duration.ofMillis(((Number) conversion.source).longValue());
        } else if (conversion.source instanceof CharSequence || conversion.source instanceof Character) {
            try {
                return Duration.ofMillis(Long.parseLong((conversion.source.toString().trim())));
            } catch (NumberFormatException e) {
            }
        }
        return null;
    }

    @Override
    public Class<?> targetType() {
        return Duration.class;
    }
}
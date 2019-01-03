package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import org.junit.Assert;
import org.junit.Test;

import java.time.*;
import java.util.Calendar;
import java.util.Date;

public class DateTest {

    @Test
    public void testDuration() {
        DurationConverter converter = new DurationConverter();
        Duration duration = converter.execute(new Conversion(String.class, Duration.class, "1234567"));
        Assert.assertNotNull(duration);
        duration = converter.execute(new Conversion(Number.class, Duration.class, 1000L));
        Assert.assertNotNull(duration);
        duration = converter.execute(new Conversion(Character.class, Duration.class, '1'));
        Assert.assertNotNull(duration);
    }

    @Test
    public void testInstant() {
        InstantConverter converter = new InstantConverter();
        Instant instant = converter.execute(new Conversion(String.class, Instant.class, "1234567"));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(Number.class, Instant.class, 1000L));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(Character.class, Instant.class, '1'));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(Calendar.class, Instant.class, Calendar.getInstance()));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(Date.class, Instant.class, new Date()));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(LocalDateTime.class, Instant.class, LocalDateTime.now()));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(String.class, Instant.class, "2018-10-01"));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(String.class, Instant.class, "2018-10-01 10:10:10"));
        Assert.assertNotNull(instant);
        instant = converter.execute(new Conversion(String.class, Instant.class, "2018-10-01T10:10:10"));
        Assert.assertNotNull(instant);
    }

    @Test
    public void testDate() {
        Date8Converter converter = new Date8Converter();
        Date date = converter.execute(new Conversion(String.class, Date.class, "1234567"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Number.class, Date.class, 1000L));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Character.class, Date.class, '1'));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Calendar.class, Date.class, Calendar.getInstance()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Date.class, Date.class, new Date()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(LocalDateTime.class, Date.class, LocalDateTime.now()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, Date.class, "2018-10-01"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, Date.class, "2018-10-01", "yyyy-MM-dd"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, Date.class, "2018-10-01 10:10:10"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, Date.class, "2018-10-01T10:10:10"));
        Assert.assertNotNull(date);
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTimeConverter converter = new LocalDateTimeConverter();
        LocalDateTime date = converter.execute(new Conversion(String.class, LocalDateTime.class, "1234567"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Number.class, LocalDateTime.class, 1000L));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Character.class, LocalDateTime.class, '1'));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Calendar.class, LocalDateTime.class, Calendar.getInstance()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Date.class, LocalDateTime.class, new Date()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(LocalDateTime.class, LocalDateTime.class, LocalDateTime.now()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDateTime.class, "2018-10-01"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDateTime.class, "2018-10-01", "yyyy-MM-dd"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDateTime.class, "2018-10-01 10:10:10"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDateTime.class, "2018-10-01T10:10:10"));
        Assert.assertNotNull(date);
    }

    @Test
    public void testLocalDate() {
        LocalDateConverter converter = new LocalDateConverter();
        LocalDate date = converter.execute(new Conversion(String.class, LocalDate.class, "1234567"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Number.class, LocalDate.class, 1000L));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Character.class, LocalDate.class, '1'));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Calendar.class, LocalDate.class, Calendar.getInstance()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Date.class, LocalDate.class, new Date()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(LocalDateTime.class, LocalDate.class, LocalDateTime.now()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDate.class, "2018-10-01"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDate.class, "2018-10-01", "yyyy-MM-dd"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDate.class, "2018-10-01 10:10:10"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalDate.class, "2018-10-01T10:10:10"));
        Assert.assertNotNull(date);
    }

    @Test
    public void testLocalTime() {
        LocalTimeConverter converter = new LocalTimeConverter();
        LocalTime date = converter.execute(new Conversion(String.class, LocalTime.class, "1234567"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Number.class, LocalTime.class, 1000L));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Character.class, LocalTime.class, '1'));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Calendar.class, LocalTime.class, Calendar.getInstance()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(Date.class, LocalTime.class, new Date()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(LocalDateTime.class, LocalTime.class, LocalDateTime.now()));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalTime.class, "2018-10-01"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalTime.class, "2018-10-01", "yyyy-MM-dd"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalTime.class, "2018-10-01 10:10:10"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalTime.class, "2018-10-01T10:10:10"));
        Assert.assertNotNull(date);
        date = converter.execute(new Conversion(String.class, LocalTime.class, "10:10:10"));
        Assert.assertNotNull(date);
    }


}

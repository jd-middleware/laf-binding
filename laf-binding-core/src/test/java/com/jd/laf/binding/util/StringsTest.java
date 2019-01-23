package com.jd.laf.binding.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class StringsTest {

    @Test
    public void testProperties() {
        Properties properties = new Properties();
        properties.put("1", "2");
        String value = Strings.toString(properties);
        Assert.assertEquals(value, "1=2");
    }
}

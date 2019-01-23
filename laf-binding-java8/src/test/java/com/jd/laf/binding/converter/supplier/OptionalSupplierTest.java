package com.jd.laf.binding.converter.supplier;

import com.jd.laf.binding.Binding;
import com.jd.laf.binding.annotation.Value;
import com.jd.laf.binding.reflect.exception.ReflectionException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class OptionalSupplierTest {

    @Test
    public void testOptional() throws ReflectionException {
        Map<String, Object> context = new HashMap<>();
        context.put("threads", "123");
        Config config = new Config();
        Binding.bind(context, config);
        Optional<Long> threads = config.getThreads();
        Assert.assertNotNull(threads);
        Long value = threads.get();
        Assert.assertNotNull(value);
        Assert.assertEquals(value.longValue(), 123L);
    }

    public static class Config {
        @Value
        private Optional<Long> threads;

        public Optional<Long> getThreads() {
            return threads;
        }

        public void setThreads(Optional<Long> threads) {
            this.threads = threads;
        }
    }
}

package com.jd.laf.binding.converter.simple;

import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.SimpleConverter;

import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

/**
 * Properties转换器
 */
public class PropertiesConverter implements SimpleConverter {

    @Override
    public Properties execute(final Conversion conversion) {
        if (conversion == null) {
            return null;
        }
        Object value = conversion.source;
        if (value != null && value instanceof CharSequence) {
            Properties properties = new Properties();
            StringReader reader = new StringReader(value.toString());
            try {
                properties.load(reader);
                return properties;
            } catch (IOException e) {
            } finally {
                reader.close();
            }
        }
        return null;
    }

    @Override
    public boolean support(Class<?> sourceType) {
        return sourceType != null && CharSequence.class.isAssignableFrom(sourceType);
    }

    @Override
    public Class<?> targetType() {
        return Properties.class;
    }
}

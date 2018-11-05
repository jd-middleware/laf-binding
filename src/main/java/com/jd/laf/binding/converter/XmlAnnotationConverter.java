package com.jd.laf.binding.converter;

import com.jd.laf.binding.annotation.XmlConverter;
import com.jd.laf.binding.marshaller.Unmarshaller;
import com.jd.laf.binding.marshaller.XmlProvider;
import com.jd.laf.binding.marshaller.XmlProviders;

public class XmlAnnotationConverter implements AnnotationConverter {

    @Override
    public boolean support(final Class<?> sourceType, final Class<?> targetType) {
        return CharSequence.class.isAssignableFrom(sourceType);
    }

    @Override
    public Class<?> annotation() {
        return XmlConverter.class;
    }

    @Override
    public Object execute(final Conversion conversion) throws Exception {
        //获取JSON插件
        XmlProvider plugin = XmlProviders.getPlugin();
        Unmarshaller unmarshaller = plugin == null ? null : plugin.getUnmarshaller();
        if (unmarshaller == null) {
            return null;
        }
        //反序列化
        return unmarshaller.unmarshall(conversion.getSource().toString(), conversion.getTargetType(), null);
    }
}


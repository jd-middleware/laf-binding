package com.jd.laf.binding.converter.transformer;

import com.jd.laf.binding.annotation.JsonConverter;
import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.Transformer;
import com.jd.laf.binding.marshaller.JsonProvider;
import com.jd.laf.binding.marshaller.JsonProviders;
import com.jd.laf.binding.marshaller.Unmarshaller;

/**
 * JSON转换器
 */
public class JsonTransformer implements Transformer {

    @Override
    public boolean support(final Class<?> sourceType, final Class<?> targetType) {
        return CharSequence.class.isAssignableFrom(sourceType);
    }

    @Override
    public Class<?> annotation() {
        return JsonConverter.class;
    }

    @Override
    public Object execute(final Conversion conversion) throws Exception {
        //获取JSON插件
        JsonProvider plugin = JsonProviders.getPlugin();
        Unmarshaller unmarshaller = plugin == null ? null : plugin.getUnmarshaller();
        if (unmarshaller == null) {
            return null;
        }
        //反序列化
        return unmarshaller.unmarshall(conversion.source.toString(), conversion.targetType, null);
    }
}


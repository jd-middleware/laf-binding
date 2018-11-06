package com.jd.laf.binding.converter.supplier;


import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;
import com.jd.laf.binding.converter.Transformer;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 注解转换器提供者
 * Created by hexiaofeng on 16-8-29.
 */
public class TransformerSupplier implements ConverterSupplier {

    ///注解类对应的绑定器
    protected static volatile Map<Class<?>, Transformer> plugins;
    protected static ConcurrentMap<ConversionType, Option<Transformer>> transformers = new ConcurrentHashMap<ConversionType, Option<Transformer>>();

    @Override
    public Converter getConverter(final ConversionType type) {
        if (type == null || type.scope == null) {
            return null;
        }

        // 从缓存中获取
        Option<Transformer> option = transformers.get(type);
        if (option == null) {
            // 从插件中查找匹配的转换器
            // 提前构造选项对象，没有找到匹配对象的时候放一个无值的选项对象
            option = new Option<Transformer>();
            Map<Class<?>, Transformer> plugins = getPlugins();
            Annotation[] annotations = type.scope.getAnnotations();

            Transformer converter;
            for (Annotation annotation : annotations) {
                converter = plugins.get(annotation.annotationType());
                if (converter != null && converter.support(type.sourceType, type.targetType)) {
                    option.setValue(converter);
                    break;
                }
            }
            Option<Transformer> exists = transformers.putIfAbsent(type, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

    @Override
    public int order() {
        return TRANSFORMER_SUPPLIER_ORDER;
    }

    protected static Map<Class<?>, Transformer> getPlugins() {
        //加载插件
        if (plugins == null) {
            synchronized (TransformerSupplier.class) {
                if (plugins == null) {
                    Map<Class<?>, Transformer> map = new HashMap<Class<?>, Transformer>();
                    //加载插件
                    ServiceLoader<Transformer> loader = ServiceLoader.load(Transformer.class, TransformerSupplier.class.getClassLoader());
                    for (Transformer AnnotationConverter : loader) {
                        map.put(AnnotationConverter.annotation(), AnnotationConverter);
                    }
                    plugins = map;
                }
            }
        }
        return plugins;
    }

}

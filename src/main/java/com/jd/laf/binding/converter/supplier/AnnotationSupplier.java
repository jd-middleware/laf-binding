package com.jd.laf.binding.converter.supplier;


import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.AnnotationConverter;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;

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
public class AnnotationSupplier implements ConverterSupplier {

    ///注解类对应的绑定器
    protected static volatile Map<Class<?>, AnnotationConverter> plugins;
    protected static ConcurrentMap<ConversionType, Option<AnnotationConverter>> typeConverters = new ConcurrentHashMap<ConversionType, Option<AnnotationConverter>>();

    @Override
    public Converter getConverter(final ConversionType type) {
        if (type == null || type.getScope() == null) {
            return null;
        }

        // 从缓存中获取
        Option<AnnotationConverter> option = typeConverters.get(type);
        if (option == null) {
            // 没有找到则从注解中查找
            option = new Option<AnnotationConverter>();
            Map<Class<?>, AnnotationConverter> plugins = getPlugins();
            Annotation[] annotations = type.getScope().getAnnotations();

            AnnotationConverter converter;
            for (Annotation annotation : annotations) {
                converter = plugins.get(annotation.annotationType());
                if (converter != null && converter.support(type.getSourceType(), type.getTargetType())) {
                    option.setValue(converter);
                    break;
                }
            }
            Option<AnnotationConverter> exists = typeConverters.putIfAbsent(type, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

    @Override
    public int order() {
        return ANNOTATION_SUPPLIER_ORDER;
    }

    protected static Map<Class<?>, AnnotationConverter> getPlugins() {
        //加载插件
        if (plugins == null) {
            synchronized (AnnotationSupplier.class) {
                if (plugins == null) {
                    Map<Class<?>, AnnotationConverter> map = new HashMap<Class<?>, AnnotationConverter>();
                    //加载插件
                    ServiceLoader<AnnotationConverter> loader = ServiceLoader.load(AnnotationConverter.class, AnnotationSupplier.class.getClassLoader());
                    for (AnnotationConverter AnnotationConverter : loader) {
                        map.put(AnnotationConverter.annotation(), AnnotationConverter);
                    }
                    plugins = map;
                }
            }
        }
        return plugins;
    }

}

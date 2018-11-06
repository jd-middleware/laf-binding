package com.jd.laf.binding.converter.supplier;


import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.ConversionType;
import com.jd.laf.binding.converter.Converter;
import com.jd.laf.binding.converter.ConverterSupplier;
import com.jd.laf.binding.converter.SimpleConverter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 简单类型转换器提供者
 * Created by hexiaofeng on 16-8-29.
 */
public class SimpleSupplier implements ConverterSupplier {

    // 转换器
    protected static volatile Map<Class<?>, List<SimpleConverter>> plugins;
    protected static ConcurrentMap<ConversionType, Option<SimpleConverter>> converters =
            new ConcurrentHashMap<ConversionType, Option<SimpleConverter>>();

    @Override
    public Converter getConverter(final ConversionType type) {

        //去掉Scope作用域
        ConversionType key = new ConversionType(type.sourceType, type.targetType);

        //判断是否有转化器
        Option<SimpleConverter> option = converters.get(key);
        if (option == null) {
            option = new Option<SimpleConverter>();
            //没有缓存，则重新计算
            List<SimpleConverter> converters = getPlugins().get(type.targetType);
            if (converters != null) {
                for (SimpleConverter converter : converters) {
                    if (converter.support(type.sourceType)) {
                        option.setValue(converter);
                        break;
                    }
                }
            }
            //缓存
            Option<SimpleConverter> exists = SimpleSupplier.converters.putIfAbsent(key, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

    @Override
    public int order() {
        return SIMPLE_SUPPLIER_ORDER;
    }

    /**
     * 加载插件
     *
     * @return
     */
    protected static Map<Class<?>, List<SimpleConverter>> getPlugins() {
        //加载插件
        if (plugins == null) {
            synchronized (SimpleSupplier.class) {
                if (plugins == null) {
                    Map<Class<?>, List<SimpleConverter>> map = new HashMap<Class<?>, List<SimpleConverter>>();
                    //加载转换器插件
                    ServiceLoader<SimpleConverter> loader = ServiceLoader.load(SimpleConverter.class, SimpleSupplier.class.getClassLoader());
                    for (SimpleConverter converter : loader) {
                        List<SimpleConverter> list = map.get(converter.targetType());
                        if (list == null) {
                            list = new ArrayList<SimpleConverter>(1);
                            map.put(converter.targetType(), list);
                        }
                        list.add(converter);
                    }
                    plugins = map;

                }
            }
        }
        return plugins;
    }
}

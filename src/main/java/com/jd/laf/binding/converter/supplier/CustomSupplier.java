package com.jd.laf.binding.converter.supplier;


import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.Conversion;
import com.jd.laf.binding.converter.Converter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自定义转换器提供者
 * Created by hexiaofeng on 16-8-29.
 */
public class CustomSupplier implements ConverterSupplier {

    // 转换器
    protected static volatile Map<Class<?>, List<Converter>> plugins;
    protected static ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Converter>>> converters =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<Class<?>, Option<Converter>>>();

    @Override
    public Operation getOperation(final Class<?> sourceType, final Class<?> targetType) {
        Converter converter = getConverter(sourceType, targetType);
        return converter == null ? null : new CustomOperation(converter);
    }

    @Override
    public int order() {
        return CUSTOM_SUPPLIER_ORDER;
    }

    /**
     * 获取转换器
     *
     * @param src    源对象
     * @param target 目标对象
     * @return
     */
    protected static Converter getConverter(final Class<?> src, final Class<?> target) {
        //加载插件
        if (plugins == null) {
            synchronized (CustomSupplier.class) {
                if (plugins == null) {
                    Map<Class<?>, List<Converter>> map = new HashMap<Class<?>, List<Converter>>();
                    //加载转换器插件
                    ServiceLoader<Converter> loader = ServiceLoader.load(Converter.class, CustomSupplier.class.getClassLoader());
                    for (Converter converter : loader) {
                        List<Converter> list = map.get(converter.type());
                        if (list == null) {
                            list = new ArrayList<Converter>(1);
                            map.put(converter.type(), list);
                        }
                        list.add(converter);
                    }
                    plugins = map;

                }
            }
        }

        //判断是否有转化器
        ConcurrentMap<Class<?>, Option<Converter>> options = converters.get(target);
        if (options == null) {
            options = new ConcurrentHashMap<Class<?>, Option<Converter>>();
            ConcurrentMap<Class<?>, Option<Converter>> exists = converters.putIfAbsent(target, options);
            if (exists != null) {
                options = exists;
            }
        }
        Option<Converter> option = options.get(src);
        if (option == null) {
            //没有缓存，则重新计算
            List<Converter> convs = plugins.get(target);
            if (convs != null) {
                for (Converter converter : convs) {
                    if (converter.support(src)) {
                        option = new Option<Converter>(converter);
                        break;
                    }
                }
            }
            //没有找到转换器，则设置一个空选项
            if (option == null) {
                option = new Option<Converter>();
            }
            //缓存
            Option<Converter> exists = options.putIfAbsent(src, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

    /**
     * 自定义转换器转换操作
     */
    protected static final class CustomOperation implements Operation {

        protected final Converter converter;

        public CustomOperation(Converter converter) {
            this.converter = converter;
        }

        @Override
        public Object execute(final Conversion conversion) throws Exception {
            return conversion == null ? null : converter.convert(conversion);
        }
    }
}

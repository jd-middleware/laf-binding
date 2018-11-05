package com.jd.laf.binding.converter;


import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.supplier.ConverterSupplier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 自定义转换器提供者
 * Created by hexiaofeng on 16-8-29.
 */
public abstract class Converters {

    //转换器
    protected static volatile List<ConverterSupplier> plugins;
    protected static final ConcurrentMap<ConversionType, Option<Converter>> operations =
            new ConcurrentHashMap<ConversionType, Option<Converter>>();
    //不做转换
    protected static final Converter NONE = new Converter() {
        @Override
        public Object execute(Conversion conversion) throws Exception {
            return conversion.source;
        }
    };

    /**
     * 获取转换操作
     *
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 转换操作
     */
    public static Converter getPlugin(final Class<?> sourceType, final Class<?> targetType) {
        return getPlugin(new ConversionType(sourceType, targetType));
    }

    /**
     * 获取转换操作
     *
     * @param type 转换类型
     * @return 转换操作
     */
    public static Converter getPlugin(final ConversionType type) {
        if (type == null) {
            return null;
        } else if (type.getTargetType() == type.getSourceType() || type.getTargetType().isAssignableFrom(type.getSourceType())) {
            //可以直接赋值
            return NONE;
        } else if (plugins == null) {
            //加载插件
            synchronized (Converters.class) {
                if (plugins == null) {
                    List<ConverterSupplier> suppliers = new ArrayList<ConverterSupplier>();
                    //加载转换器插件
                    ServiceLoader<ConverterSupplier> loader = ServiceLoader.load(ConverterSupplier.class, Converters.class.getClassLoader());
                    for (ConverterSupplier supplier : loader) {
                        suppliers.add(supplier);
                    }
                    Collections.sort(suppliers, new Comparator<ConverterSupplier>() {
                        @Override
                        public int compare(ConverterSupplier o1, ConverterSupplier o2) {
                            return o1.order() - o2.order();
                        }
                    });
                    plugins = suppliers;
                }
            }
        }

        //判断是否有转化器
        Option<Converter> option = operations.get(type);
        if (option == null) {
            //没有缓存，则重新计算
            Converter operation = null;
            for (ConverterSupplier plugin : plugins) {
                operation = plugin.getConverter(type);
                if (operation != null) {
                    break;
                }
            }
            option = new Option<Converter>(operation);
            //缓存
            Option<Converter> exists = operations.putIfAbsent(type, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }


}

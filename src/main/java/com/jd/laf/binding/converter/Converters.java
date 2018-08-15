package com.jd.laf.binding.converter;


import com.jd.laf.binding.Option;
import com.jd.laf.binding.converter.ConverterSupplier.Operation;

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
    protected static final ConcurrentMap<Class<?>, ConcurrentMap<Class<?>, Option<Operation>>> operations =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<Class<?>, Option<Operation>>>();
    //不做转换
    protected static final Operation NONE = new Operation() {
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
    public static Operation getOperation(final Class<?> sourceType, final Class<?> targetType) {
        if (sourceType == null || targetType == null) {
            return null;
        } else if (targetType == sourceType || targetType.isAssignableFrom(sourceType)) {
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
        ConcurrentMap<Class<?>, Option<Operation>> options = operations.get(targetType);
        if (options == null) {
            options = new ConcurrentHashMap<Class<?>, Option<Operation>>();
            ConcurrentMap<Class<?>, Option<Operation>> exists = operations.putIfAbsent(targetType, options);
            if (exists != null) {
                options = exists;
            }
        }
        Option<Operation> option = options.get(sourceType);
        if (option == null) {
            //没有缓存，则重新计算
            Operation operation = null;
            for (ConverterSupplier converter : plugins) {
                operation = converter.getOperation(sourceType, targetType);
                if (operation != null) {
                    break;
                }
            }
            option = new Option<Operation>(operation);
            //缓存
            Option<Operation> exists = options.putIfAbsent(sourceType, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

}

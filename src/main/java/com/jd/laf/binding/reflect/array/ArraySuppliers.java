package com.jd.laf.binding.reflect.array;

import com.jd.laf.binding.Option;
import com.jd.laf.binding.reflect.array.supplier.ArraySupplier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 属性获取管理器
 */
public abstract class ArraySuppliers {

    //属性获取器插件
    protected static volatile List<ArraySupplier> plugins;
    //类对应的属性获取器
    protected static ConcurrentMap<Class<?>, Option<ArraySupplier>> suppliers =
            new ConcurrentHashMap<Class<?>, Option<ArraySupplier>>();

    /**
     * 获取属性获取器
     *
     * @param clazz 类型
     * @return
     */
    public static ArraySupplier getArraySupplier(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (plugins == null) {
            //加载插件
            synchronized (ArraySuppliers.class) {
                if (plugins == null) {
                    List<ArraySupplier> result = new ArrayList<ArraySupplier>();
                    //加载插件
                    ClassLoader classLoader = ArraySuppliers.class.getClassLoader();
                    ServiceLoader<ArraySupplier> loader = ServiceLoader.load(ArraySupplier.class, classLoader);
                    for (ArraySupplier supplier : loader) {
                        result.add(supplier);
                    }
                    //排序
                    Collections.sort(result, new Comparator<ArraySupplier>() {
                        @Override
                        public int compare(ArraySupplier o1, ArraySupplier o2) {
                            return o1.order() - o2.order();
                        }
                    });
                    plugins = result;
                }
            }
        }

        //获取适合的插件
        Option<ArraySupplier> option = suppliers.get(clazz);
        if (option == null) {
            ArraySupplier supplier = null;
            for (ArraySupplier plugin : plugins) {
                if (plugin.support(clazz)) {
                    supplier = plugin;
                    break;
                }
            }
            option = new Option<ArraySupplier>(supplier);
            Option<ArraySupplier> exists = suppliers.putIfAbsent(clazz, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }
}

package com.jd.laf.binding.reflect;

import com.jd.laf.binding.Option;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 属性获取管理器
 */
public abstract class PropertySuppliers {

    public static final String GET_OBJECT = "getObject";
    //属性获取器插件
    protected static volatile List<PropertySupplier> plugins;
    //类对应的属性获取器
    protected static ConcurrentMap<Class<?>, Option<PropertySupplier>> suppliers =
            new ConcurrentHashMap<Class<?>, Option<PropertySupplier>>();

    /**
     * 获取插件
     *
     * @return
     */
    protected static List<PropertySupplier> getPlugins() {
        if (plugins == null) {
            //加载插件
            synchronized (PropertySuppliers.class) {
                if (plugins == null) {
                    List<PropertySupplier> result = new ArrayList<PropertySupplier>();
                    //加载插件
                    ClassLoader classLoader = PropertySuppliers.class.getClassLoader();
                    ServiceLoader<PropertySupplier> loader = ServiceLoader.load(PropertySupplier.class, classLoader);
                    for (PropertySupplier getter : loader) {
                        result.add(getter);
                    }
                    plugins = result;
                }
            }
        }
        return plugins;
    }

    /**
     * 获取属性获取器
     *
     * @param clazz 类型
     * @return
     */
    public static PropertySupplier getPlugin(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }

        List<PropertySupplier> plugins = getPlugins();

        //获取适合的插件
        Option<PropertySupplier> option = suppliers.get(clazz);
        if (option == null) {
            PropertySupplier getter = null;
            for (PropertySupplier plugin : plugins) {
                if (plugin.support(clazz)) {
                    getter = plugin;
                    break;
                }
            }
            if (getter == null) {
                //查找方法Object get(String name)
                try {
                    Method method = clazz.getMethod(GET_OBJECT, String.class);
                    if (method != null) {
                        getter = new MethodSupplier(method);
                    }
                } catch (Exception e) {
                }
            }
            option = new Option<PropertySupplier>(getter);
            Option<PropertySupplier> exists = suppliers.putIfAbsent(clazz, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

    /**
     * 根据反射方法来调用
     */
    protected static class MethodSupplier implements PropertySupplier {

        protected Method method;

        public MethodSupplier(Method method) {
            this.method = method;
        }

        @Override
        public Object get(final Object target, final String name) throws Exception {
            return method.invoke(target, name);
        }

        @Override
        public boolean support(Class<?> clazz) {
            return true;
        }
    }

}

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
public abstract class PropertyGetters {

    public static final String GET_OBJECT = "getObject";
    //属性获取器插件
    protected static volatile List<PropertyGetter> plugins;
    //类对应的属性获取器
    protected static ConcurrentMap<Class<?>, Option<PropertyGetter>> getters =
            new ConcurrentHashMap<Class<?>, Option<PropertyGetter>>();

    /**
     * 获取属性获取器
     *
     * @param clazz 类型
     * @return
     */
    public static PropertyGetter getPropertyGetter(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (plugins == null) {
            //加载插件
            synchronized (PropertyGetters.class) {
                if (plugins == null) {
                    List<PropertyGetter> result = new ArrayList<PropertyGetter>();
                    //加载插件
                    ClassLoader classLoader = PropertyGetters.class.getClassLoader();
                    ServiceLoader<PropertyGetter> loader = ServiceLoader.load(PropertyGetter.class, classLoader);
                    for (PropertyGetter getter : loader) {
                        result.add(getter);
                    }
                    plugins = result;
                }
            }
        }

        //获取适合的插件
        Option<PropertyGetter> option = getters.get(clazz);
        if (option == null) {
            PropertyGetter getter = null;
            for (PropertyGetter plugin : plugins) {
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
                        getter = new MethodGetter(method);
                    }
                } catch (Exception e) {
                }
            }
            option = new Option<PropertyGetter>(getter);
            Option<PropertyGetter> exists = getters.putIfAbsent(clazz, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

    /**
     * 根据反射方法来调用
     */
    protected static class MethodGetter implements PropertyGetter {

        protected Method method;

        public MethodGetter(Method method) {
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

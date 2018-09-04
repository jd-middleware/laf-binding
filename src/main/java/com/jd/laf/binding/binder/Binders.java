package com.jd.laf.binding.binder;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * 绑定器管理器
 */
public abstract class Binders {

    //类对应的绑定器
    protected static volatile Map<Class<?>, Binder> plugins;

    /**
     * 获取绑定器
     *
     * @param clazz 类型
     * @return
     */
    public static Binder getPlugin(final Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        if (plugins == null) {
            //加载插件
            synchronized (Binders.class) {
                if (plugins == null) {
                    Map<Class<?>, Binder> result = new HashMap<Class<?>, Binder>();
                    //加载插件
                    ServiceLoader<Binder> loader = ServiceLoader.load(Binder.class, Binders.class.getClassLoader());
                    for (Binder binder : loader) {
                        result.put(binder.annotation(), binder);
                    }
                    plugins = result;
                }
            }
        }

        //获取适合的插件
        return plugins.get(clazz);
    }

}

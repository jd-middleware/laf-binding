package com.jd.laf.binding.reflect;

import com.jd.laf.extension.Option;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 类工具
 */
public abstract class Classes {

    protected static ConcurrentMap<String, Option<Class>> map = new ConcurrentHashMap<String, Option<Class>>();

    /**
     * 获取类
     *
     * @param name 类
     * @return
     */
    public static Class getClass(final String name) {
        if (name == null) {
            return null;
        }
        Option<Class> option = map.get(name);
        if (option == null) {
            Class result;
            try {
                result = Class.forName(name);
            } catch (ClassNotFoundException e) {
                result = null;
            }
            option = new Option<Class>(result);
            Option<Class> exists = map.putIfAbsent(name, option);
            if (exists != null) {
                option = exists;
            }
        }
        return option.get();
    }

}

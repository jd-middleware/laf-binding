package com.jd.laf.binding.reflect;

import com.jd.laf.binding.util.SuperClassIterator;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 字段工具类
 */
public abstract class Fields {

    //类的字段名和字段映射
    protected static ConcurrentMap<Class<?>, ConcurrentMap<String, Field>> fields =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<String, Field>>();

    /**
     * 获取类的字段
     *
     * @param clazz 类
     * @return 字段
     */
    public static ConcurrentMap<String, Field> getFields(final Class<?> clazz) {
        if (clazz == null || clazz.isPrimitive() || clazz.isArray() || clazz.isInterface()
                || clazz.isEnum()) {
            return null;
        }

        ConcurrentMap<String, Field> options = fields.get(clazz);
        if (options == null) {
            options = new ConcurrentHashMap<String, Field>();

            SuperClassIterator iterator = new SuperClassIterator(clazz);
            while (iterator.hasNext()) {
                for (Field field : iterator.next().getDeclaredFields()) {
                    options.putIfAbsent(field.getName(), field);
                }
            }

            ConcurrentMap<String, Field> exist = fields.putIfAbsent(clazz, options);
            if (exist != null) {
                options = exist;
            }
        }
        return options;
    }

    /**
     * 获取类的字段
     *
     * @param clazz 类
     * @param name  属性名
     * @return 字段
     */
    public static Field getField(final Class<?> clazz, final String name) {
        ConcurrentMap<String, Field> options = getFields(clazz);
        return options == null ? null : options.get(name);
    }

}

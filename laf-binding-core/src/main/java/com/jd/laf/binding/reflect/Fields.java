package com.jd.laf.binding.reflect;

import com.jd.laf.binding.util.SuperClassIterator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 字段工具类
 */
public abstract class Fields {

    //类的字段名和字段映射
    protected static ConcurrentMap<Class<?>, ConcurrentMap<String, Field>> names =
            new ConcurrentHashMap<Class<?>, ConcurrentMap<String, Field>>();

    //类的字段列表
    protected static ConcurrentMap<Class<?>, List<Field>> fields =
            new ConcurrentHashMap<Class<?>, List<Field>>();

    /**
     * 获取类的字段
     *
     * @param clazz 类
     * @return 字段
     */
    public static List<Field> getFields(final Class<?> clazz) {
        if (clazz == null || clazz.isPrimitive() || clazz.isArray() || clazz.isInterface()) {
            return null;
        }

        List<Field> options = fields.get(clazz);
        if (options == null) {
            options = new ArrayList<Field>();

            SuperClassIterator iterator = new SuperClassIterator(clazz);
            while (iterator.hasNext()) {
                for (Field field : iterator.next().getDeclaredFields()) {
                    options.add(field);
                }
            }
            List<Field> exist = fields.putIfAbsent(clazz, options);
            if (exist != null) {
                options = exist;
            }
        }
        return options;
    }

    /**
     * 获取类的字段名映射
     *
     * @param clazz 类
     * @return 字段
     */
    public static ConcurrentMap<String, Field> getNames(final Class<?> clazz) {
        if (clazz == null || clazz.isPrimitive() || clazz.isArray() || clazz.isInterface()) {
            return null;
        }

        ConcurrentMap<String, Field> options = names.get(clazz);
        if (options == null) {
            options = new ConcurrentHashMap<String, Field>();
            List<Field> fields = getFields(clazz);
            if (fields != null) {
                for (Field field : fields) {
                    options.putIfAbsent(field.getName(), field);
                }
            }
            ConcurrentMap<String, Field> exist = names.putIfAbsent(clazz, options);
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
        ConcurrentMap<String, Field> options = getNames(clazz);
        return options == null ? null : options.get(name);
    }

}

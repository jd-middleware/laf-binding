package com.jd.laf.binding;


import com.jd.laf.binding.binder.Binder;
import com.jd.laf.binding.binder.Binder.Context;
import com.jd.laf.binding.reflect.FieldAccessor;
import com.jd.laf.binding.reflect.FieldAccessorFactory;
import com.jd.laf.binding.reflect.Reflect;
import com.jd.laf.binding.reflect.ReflectAccessorFactory;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.binder.Binders.getBinder;
import static com.jd.laf.binding.reflect.Reflect.getField;

/**
 * 绑定器
 * Created by hexiaofeng on 16-8-29.
 */
public class Binding {

    // 缓存类的绑定关系
    protected static ConcurrentMap<Class<?>, List<BindingField>> bindingFields = new ConcurrentHashMap<Class<?>, List<BindingField>>();

    /**
     * 设置字段值
     *
     * @param target 目标对象
     * @param field  字段名称
     * @param value  字段值
     * @throws ReflectionException
     */
    public static void set(final Object target, final String field, final Object value) throws ReflectionException {
        set(target, field, value, ReflectAccessorFactory.getInstance());
    }

    /**
     * 设置字段值
     *
     * @param target  目标对象
     * @param field   字段名称
     * @param value   字段值
     * @param factory 字段访问对象工厂
     * @throws ReflectionException
     */
    public static void set(final Object target, final String field, final Object value,
                           final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || target == null || factory == null) {
            return;
        }
        set(target, getField(target.getClass(), field), value, factory);
    }

    /**
     * 设置字段值
     *
     * @param target 目标对象
     * @param field  字段
     * @param value  字段值
     * @throws ReflectionException
     */
    public static void set(final Object target, final Field field, final Object value) throws ReflectionException {
        set(target, field, value, ReflectAccessorFactory.getInstance());
    }

    /**
     * 设置字段值
     *
     * @param target  目标对象
     * @param field   字段
     * @param value   字段值
     * @param factory 字段访问对象工厂
     * @throws ReflectionException
     */
    public static void set(final Object target, final Field field, final Object value,
                           final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || factory == null) {
            return;
        }
        Reflect.set(target, field, value, factory);
    }

    /**
     * 绑定上下文
     *
     * @param source 上下文
     * @param target 对象
     * @throws ReflectionException
     */
    public static void bind(final Object source, final Object target) throws ReflectionException {
        bind(source, target, ReflectAccessorFactory.getInstance());
    }

    /**
     * 绑定上下文
     *
     * @param source  上下文
     * @param target  对象
     * @param factory
     * @throws ReflectionException
     */
    public static void bind(final Object source, final Object target, final FieldAccessorFactory factory) throws ReflectionException {
        if (source == null || target == null || factory == null) {
            return;
        }
        Class<?> clazz = target.getClass();
        // 从缓存中获取
        List<BindingField> bindings = bindingFields.get(clazz);
        if (bindings == null) {
            // 没有找到则从注解中查找
            bindings = new ArrayList<BindingField>();
            Field[] fields;
            Annotation[] annotations;
            BindingField bindingField;
            Binder binder;
            // 递归查找
            while (clazz != null && clazz != Object.class) {
                //所有声明的字段
                fields = clazz.getDeclaredFields();
                //遍历字段
                for (Field field : fields) {
                    bindingField = null;
                    //遍历注解
                    annotations = field.getAnnotations();
                    for (Annotation annotation : annotations) {
                        //是否是绑定注解
                        binder = getBinder(annotation.annotationType());
                        if (binder != null) {
                            if (bindingField == null) {
                                bindingField = new BindingField(field, factory.getAccessor(field));
                            }
                            bindingField.add(new BinderAnnotation(annotation, binder));
                        }
                    }
                    //有绑定注解
                    if (bindingField != null) {
                        bindings.add(bindingField);
                    }
                }
                clazz = clazz.getSuperclass();
            }
            List<BindingField> exists = bindingFields.putIfAbsent(clazz, bindings);
            if (exists != null) {
                bindings = exists;
            }
        }
        for (BindingField binding : bindings) {
            binding.bind(source, target, factory);
        }
    }

    /**
     * 绑定字段
     */
    protected static class BindingField implements FieldAccessor {
        //字段
        final protected Field field;
        //绑定实现
        final protected List<BinderAnnotation> annotations = new ArrayList<BinderAnnotation>(2);
        //字段访问器
        final protected FieldAccessor accessor;

        public BindingField(Field field, FieldAccessor accessor) {
            this.field = field;
            this.accessor = accessor;
        }

        public Field getField() {
            return field;
        }

        public Class<?> getType() {
            return field.getType();
        }

        public FieldAccessor getAccessor() {
            return accessor;
        }

        public List<BinderAnnotation> getAnnotations() {
            return annotations;
        }

        public void add(final BinderAnnotation annotation) {
            if (annotation != null) {
                annotations.add(annotation);
            }
        }

        @Override
        public Object get(final Object target) throws ReflectionException {
            return accessor.get(target);
        }

        @Override
        public void set(Object target, Object value) throws ReflectionException {
            accessor.set(target, value);
        }

        /**
         * 绑定
         *
         * @param source
         * @param target
         * @param factory
         * @throws ReflectionException
         */
        public void bind(final Object source, final Object target, final FieldAccessorFactory factory) throws ReflectionException {
            Context context;
            for (BinderAnnotation annotation : annotations) {
                context = new Context(target, field, annotation.annotation, factory, source);
                if (annotation.binder.bind(context)) {
                    return;
                }
            }

        }
    }

    /**
     * 注解绑定器
     */
    protected static class BinderAnnotation {
        final protected Annotation annotation;
        final protected Binder binder;

        public BinderAnnotation(Annotation annotation, Binder binder) {
            this.annotation = annotation;
            this.binder = binder;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public Binder getBinder() {
            return binder;
        }
    }

}

package com.jd.laf.binding;


import com.jd.laf.binding.annotation.Value;
import com.jd.laf.binding.binder.Binder;
import com.jd.laf.binding.binder.Binder.Context;
import com.jd.laf.binding.converter.Scope;
import com.jd.laf.binding.converter.Scope.FieldScope;
import com.jd.laf.binding.converter.Scope.ParameterScope;
import com.jd.laf.binding.reflect.*;
import com.jd.laf.binding.reflect.PropertySupplier.FieldSupplier;
import com.jd.laf.binding.reflect.exception.ReflectionException;
import com.jd.laf.binding.util.Function;
import com.jd.laf.binding.util.Predicate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.jd.laf.binding.Plugin.*;
import static com.jd.laf.binding.reflect.Fields.getField;
import static com.jd.laf.binding.reflect.Fields.getFields;
import static com.jd.laf.binding.util.Collections.computeIfAbsent;

/**
 * 绑定器
 * Created by hexiaofeng on 16-8-29.
 */
public abstract class Binding {

    // 缓存类的字段绑定关系
    protected static ConcurrentMap<Class<?>, List<BindingScope>> FIELDS = new ConcurrentHashMap<Class<?>, List<BindingScope>>();

    // 缓存方法的参数绑定关系
    protected static ConcurrentMap<Class<?>, ConcurrentMap<Method, List<BindingScope>>> METHODS = new ConcurrentHashMap<Class<?>, ConcurrentMap<Method, List<BindingScope>>>();

    /**
     * 设置字段值
     *
     * @param target 目标对象
     * @param field  字段名称
     * @param value  字段值
     * @return 成功标识
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final String field, final Object value) throws ReflectionException {
        return set(target, field, value, FIELD.get());
    }

    /**
     * 设置字段值
     *
     * @param target  目标对象
     * @param field   字段名称
     * @param value   字段值
     * @param factory 字段访问对象工厂
     * @return 成功标识
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final String field, final Object value,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || target == null || factory == null) {
            return false;
        }
        return set(target, getField(target.getClass(), field), value, factory);
    }

    /**
     * 设置字段值
     *
     * @param target 目标对象
     * @param field  字段
     * @param value  字段值
     * @return 成功标识
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value) throws ReflectionException {
        return set(target, field, value, FIELD.get());
    }

    /**
     * 设置字段值
     *
     * @param target  目标对象
     * @param field   字段
     * @param value   字段值
     * @param factory 字段访问对象工厂
     * @return 成功标识
     * @throws ReflectionException
     */
    public static boolean set(final Object target, final Field field, final Object value,
                              final FieldAccessorFactory factory) throws ReflectionException {
        if (field == null || factory == null) {
            return false;
        }
        return Reflect.set(target, field, value, factory);
    }

    /**
     * 是否支持类型转换
     *
     * @param source
     * @param target
     * @return
     */
    public static boolean support(final Class<?> source, final Class<?> target) {
        return Reflect.support(source, target);
    }

    /**
     * 在指定作用域上是否支持类型转换，支持作用域上的转换注解
     *
     * @param source 源类型
     * @param target 目标类型
     * @param scope  作用域
     * @return
     */
    public static boolean support(final Class<?> source, final Class<?> target, final Scope scope) {
        return Reflect.support(source, target, scope);
    }

    /**
     * 通过上下文，绑定字段
     *
     * @param source 上下文
     * @param target 对象
     * @throws ReflectionException
     */
    public static void bind(final Object source, final Object target) throws ReflectionException {
        bind(source, target, NoneFinalField.INSTANCE, FIELD.get());
    }

    /**
     * 通过上下文，绑定字段
     *
     * @param source  上下文
     * @param target  对象
     * @param factory
     * @throws ReflectionException
     */
    public static void bind(final Object source, final Object target, final FieldAccessorFactory factory) throws ReflectionException {
        bind(source, target, NoneFinalField.INSTANCE, factory);
    }

    /**
     * 通过上下文，绑定字段
     *
     * @param source    上下文
     * @param target    对象
     * @param predicate 预测
     * @param factory   字段访问工厂类
     * @throws ReflectionException
     */
    public static void bind(final Object source, final Object target, final Predicate<Field> predicate,
                            final FieldAccessorFactory factory) throws ReflectionException {
        if (source == null || target == null) {
            return;
        }
        Class<?> clazz = target.getClass();
        // 从缓存中获取
        List<BindingScope> bindings = computeIfAbsent(FIELDS, clazz, new Function<Class<?>, List<BindingScope>>() {
            @Override
            public List<BindingScope> apply(final Class<?> clazz) {
                // 没有找到则从注解中查找
                List<BindingScope> bindings = new ArrayList<BindingScope>();
                Annotation[] annotations;
                BindingScope bindingField;
                Binder binder;
                //字段过滤
                Predicate myPredicate = predicate == null ? NoneFinalField.INSTANCE : predicate;
                FieldAccessorFactory myFactory = factory == null ? FIELD.get() : factory;
                //获取所有字段
                List<Field> fields = getFields(clazz);
                if (fields != null) {
                    //遍历字段
                    for (Field field : fields) {
                        //判断是否要过滤掉字段
                        if (!myPredicate.test(field)) {
                            continue;
                        }
                        bindingField = null;
                        //遍历注解
                        annotations = field.getAnnotations();
                        for (Annotation annotation : annotations) {
                            //是否是绑定注解
                            binder = BINDER.get(annotation.annotationType());
                            if (binder != null) {
                                if (bindingField == null) {
                                    bindingField = new BindingScope(new FieldScope(clazz, field, myFactory.getAccessor(field)),
                                            new FieldSupplier(factory));
                                }
                                bindingField.add(new BinderAnnotation(annotation, binder));
                            }
                        }
                        //有绑定注解
                        if (bindingField != null) {
                            bindings.add(bindingField);
                        }
                    }
                }
                return bindings;
            }
        });
        for (BindingScope binding : bindings) {
            binding.bind(source, target);
        }
    }

    /**
     * 绑定目标对象参数上下文
     *
     * @param source 上下文
     * @param target 目标对象
     * @param method 方法
     * @throws ReflectionException
     */
    public static Object[] bind(final Object source, final Object target, final Method method) throws ReflectionException {
        return bind(source, target, method, (PropertySupplier) null);
    }

    /**
     * 绑定参数上下文
     *
     * @param source   上下文
     * @param target   目标对象
     * @param method   方法
     * @param supplier 可选参数值提供者
     * @throws ReflectionException
     */
    public static Object[] bind(final Object source, final Object target, final Method method, final PropertySupplier supplier) throws ReflectionException {
        if (source == null || method == null) {
            return null;
        }
        final MethodFactory methodFactory = METHOD_FACTORY.get();
        Object[] args = new Object[methodFactory.getParameterCount(method)];

        // 按实际类来缓存方法的绑定对象
        final Class<?> targetClass = target.getClass();
        ConcurrentMap<Method, List<BindingScope>> methods = computeIfAbsent(METHODS, targetClass,
                new Function<Class<?>, ConcurrentMap<Method, List<BindingScope>>>() {
                    @Override
                    public ConcurrentMap<Method, List<BindingScope>> apply(final Class<?> key) {
                        return new ConcurrentHashMap<Method, List<BindingScope>>();
                    }
                });
        //获取该方法的的绑定对象
        List<BindingScope> bindings = computeIfAbsent(methods, method, new Function<Method, List<BindingScope>>() {
            @Override
            public List<BindingScope> apply(final Method method) {
                // 没有找到则从注解中查找
                List<BindingScope> bindings = new ArrayList<BindingScope>();
                Annotation[] annotations;
                BindingScope bindingScope;
                Binder binder;
                List<MethodParameter> parameters = methodFactory.getParameters(targetClass, method);
                //遍历字段
                for (final MethodParameter parameter : parameters) {
                    bindingScope = new BindingScope(new ParameterScope(parameter), supplier);
                    //遍历注解
                    annotations = parameter.getAnnotations();
                    for (Annotation annotation : annotations) {
                        //是否是绑定注解
                        binder = BINDER.get(annotation.annotationType());
                        if (binder != null) {
                            bindingScope.add(new BinderAnnotation(annotation, binder));
                        }
                    }
                    if (bindingScope.isEmpty()) {
                        //添加默认绑定注解
                        bindingScope.add(new BinderAnnotation(new Value() {
                            @Override
                            public String value() {
                                return parameter.getName();
                            }

                            @Override
                            public String format() {
                                return "";
                            }

                            @Override
                            public boolean nullable() {
                                return false;
                            }

                            @Override
                            public String defaultValue() {
                                return "";
                            }

                            @Override
                            public Class<? extends Annotation> annotationType() {
                                return Value.class;
                            }
                        }, BINDER.get(Value.class)));
                    }
                    bindings.add(bindingScope);
                }
                return bindings;
            }
        });
        for (BindingScope binding : bindings) {
            binding.bind(source, args);
        }
        return args;
    }

    /**
     * 绑定作用域
     */
    protected static class BindingScope {
        //字段
        protected final Scope scope;
        //字段访问器
        protected final PropertySupplier supplier;
        //绑定实现
        protected final List<BinderAnnotation> annotations = new ArrayList<BinderAnnotation>(2);

        public BindingScope(Scope scope, PropertySupplier supplier) {
            this.scope = scope;
            this.supplier = supplier;
        }

        public void add(final BinderAnnotation annotation) {
            if (annotation != null) {
                annotations.add(annotation);
            }
        }

        public boolean isEmpty() {
            return annotations.isEmpty();
        }

        /**
         * 绑定
         *
         * @param source
         * @param target
         * @throws ReflectionException
         */
        public void bind(final Object source, final Object target) throws ReflectionException {
            Context context;
            for (BinderAnnotation annotation : annotations) {
                context = new Context(source, target, annotation.annotation, scope, supplier);
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
        protected final Annotation annotation;
        protected final Binder binder;

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

    /**
     * 字段过滤
     */
    public static class NoneFinalField implements Predicate<Field> {

        public static final Predicate<Field> INSTANCE = new NoneFinalField();

        @Override
        public boolean test(Field obj) {
            return !Modifier.isFinal(obj.getModifiers());
        }
    }

}

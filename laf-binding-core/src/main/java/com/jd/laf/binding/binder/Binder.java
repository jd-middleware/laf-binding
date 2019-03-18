package com.jd.laf.binding.binder;

import com.jd.laf.binding.converter.Scope;
import com.jd.laf.binding.reflect.PropertySupplier;
import com.jd.laf.binding.reflect.Reflect;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.annotation.Annotation;

/**
 * 绑定器
 */
public interface Binder {

    /**
     * 绑定对象
     *
     * @param context 上下文
     * @return 成功标识
     * @throws ReflectionException
     */
    boolean bind(Context context) throws ReflectionException;

    /**
     * 支持的绑定注解类型
     *
     * @return 绑定注解类型
     */
    Class<?> annotation();

    /**
     * 绑定上下文
     */
    class Context {

        //源对象
        protected Object source;
        //目标对象
        protected Object target;
        //绑定注解
        protected Annotation annotation;
        //作用域
        protected Scope scope;
        //属性提供者
        protected PropertySupplier supplier;

        public Context(Object source, Object target, Annotation annotation, Scope scope, PropertySupplier supplier) {
            this.source = source;
            this.target = target;
            this.annotation = annotation;
            this.scope = scope;
            this.supplier = supplier;
        }

        public Object getSource() {
            return source;
        }

        public Object getTarget() {
            return target;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public Scope getScope() {
            return scope;
        }

        public PropertySupplier getSupplier() {
            return supplier;
        }

        /**
         * 获取名称
         *
         * @return
         */
        public String getName() {
            return scope.getName();
        }

        /**
         * 获取类型
         *
         * @return
         */
        public Class<?> getType() {
            return scope.getType();
        }

        /**
         * 获取指定名称的值
         *
         * @param name 名称
         * @return
         */
        public Object evaluate(final String name) throws ReflectionException {
            return Reflect.evaluate(source, name, supplier);
        }

        /**
         * 设置值
         *
         * @param value
         * @return
         * @throws ReflectionException
         */
        public boolean bind(final Object value) throws ReflectionException {
            return bind(value, null);
        }

        /**
         * 设置值
         *
         * @param value
         * @param format
         * @return
         * @throws ReflectionException
         */
        public boolean bind(final Object value, final String format) throws ReflectionException {
            return Reflect.set(target, scope, value, format);
        }
    }

}

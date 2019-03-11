package com.jd.laf.binding.binder;

import com.jd.laf.binding.reflect.FieldAccessorFactory;
import com.jd.laf.binding.reflect.Reflect;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

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
    abstract class Context {

        //源对象
        protected Object source;
        //目标对象
        protected Object target;
        //绑定注解
        protected Annotation annotation;

        public Context(final Object source, final Object target, final Annotation annotation) {
            this.source = source;
            this.target = target;
            this.annotation = annotation;
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

        /**
         * 获取名称
         *
         * @return
         */
        public abstract String getName();

        /**
         * 获取类型
         *
         * @return
         */
        public abstract Class<?> getType();

        /**
         * 获取指定名称的值
         *
         * @param name 名称
         * @return
         */
        public abstract Object evaluate(final String name) throws ReflectionException;

        /**
         * 设置值
         *
         * @param value
         * @return
         * @throws ReflectionException
         */
        public abstract boolean bind(final Object value) throws ReflectionException;

        /**
         * 设置值
         *
         * @param value
         * @param format
         * @return
         * @throws ReflectionException
         */
        public abstract boolean bind(final Object value, final String format) throws ReflectionException;
    }


    /**
     * 字段绑定上下文
     */
    class FieldContext extends Context {

        //字段
        protected Field field;
        //字段访问器
        protected FieldAccessorFactory factory;

        public FieldContext(final Object source, final Object target, final Annotation annotation, final Field field, final FieldAccessorFactory factory) {
            super(source, target, annotation);
            this.field = field;
            this.factory = factory;
        }

        public Field getField() {
            return field;
        }

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        public Class<?> getType() {
            return field.getType();
        }

        @Override
        public Object evaluate(final String name) throws ReflectionException {
            return Reflect.evaluate(source, name, factory);
        }

        @Override
        public boolean bind(final Object value) throws ReflectionException {
            return Reflect.set(target, field, value, factory);
        }

        @Override
        public boolean bind(final Object value, final String format) throws ReflectionException {
            return Reflect.set(target, field, value, format, factory);
        }
    }
}

package com.jd.laf.binding.binder;

import com.jd.laf.binding.reflect.FieldAccessorFactory;
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
    class Context {

        //目标对象
        private Object target;
        //字段
        private Field field;
        //绑定注解
        private Annotation annotation;
        //字段访问器
        private FieldAccessorFactory factory;
        //源对象
        private Object source;

        public Context(Object target, Field field, Annotation annotation, FieldAccessorFactory factory, Object source) {
            this.target = target;
            this.field = field;
            this.annotation = annotation;
            this.factory = factory;
            this.source = source;
        }

        public Object getTarget() {
            return target;
        }

        public Field getField() {
            return field;
        }

        public Annotation getAnnotation() {
            return annotation;
        }

        public FieldAccessorFactory getFactory() {
            return factory;
        }

        public Object getSource() {
            return source;
        }
    }
}

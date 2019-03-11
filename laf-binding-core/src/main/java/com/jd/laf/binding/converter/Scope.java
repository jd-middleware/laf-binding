package com.jd.laf.binding.converter;

import com.jd.laf.binding.reflect.FieldAccessor;
import com.jd.laf.binding.reflect.Generics;
import com.jd.laf.binding.reflect.MethodParameter;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * 作用域
 */
public interface Scope {

    /**
     * 获取名称
     *
     * @return
     */
    String getName();

    /**
     * 获取注解
     *
     * @return
     */
    Annotation[] getAnnotations();

    /**
     * 获取泛型
     *
     * @return
     */
    Class getGenericType();

    /**
     * 获取类型
     *
     * @return
     */
    Class getType();

    /**
     * 返回目标对象
     *
     * @return
     */
    Object target();

    /**
     * 更新
     *
     * @param target
     * @param value
     * @throws ReflectionException
     */
    void update(Object target, Object value) throws ReflectionException;

    /**
     * 抽象作用域
     */
    abstract class AbstractScope implements Scope {
        //注解
        protected final Annotation[] annotations;
        //泛型
        protected final Class genericType;

        public AbstractScope(Annotation[] annotations, Class genericType) {
            this.annotations = annotations;
            this.genericType = genericType;
        }

        @Override
        public Annotation[] getAnnotations() {
            return annotations;
        }

        @Override
        public Class getGenericType() {
            return genericType;
        }

    }

    /**
     * 字段作用域
     */
    class FieldScope extends AbstractScope {
        //字段
        protected final Field field;
        protected final FieldAccessor accessor;

        public FieldScope(final Field field, final FieldAccessor accessor) {
            super(field.getAnnotations(), Generics.getGenericType(field.getGenericType()));
            this.field = field;
            this.accessor = accessor;
        }

        @Override
        public String getName() {
            return field.getName();
        }

        @Override
        public Class getType() {
            return field.getType();
        }

        @Override
        public Object target() {
            return field;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            FieldScope that = (FieldScope) o;

            return field.equals(that.field);
        }

        @Override
        public int hashCode() {
            return field.hashCode();
        }

        @Override
        public void update(final Object target, final Object value) throws ReflectionException {
            accessor.set(target, value);
        }
    }

    /**
     * 参数作用域
     */
    class ParameterScope implements Scope {
        //方法
        protected final MethodParameter parameter;

        public ParameterScope(MethodParameter parameter) {
            this.parameter = parameter;
        }

        @Override
        public String getName() {
            return parameter.getName();
        }

        @Override
        public Annotation[] getAnnotations() {
            return parameter.getAnnotations();
        }

        @Override
        public Class getType() {
            return parameter.getType();
        }

        @Override
        public Class getGenericType() {
            return parameter.getGenericType();
        }

        @Override
        public Object target() {
            return parameter.target();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            ParameterScope that = (ParameterScope) o;

            return parameter.equals(that.parameter);
        }

        @Override
        public int hashCode() {
            return parameter.hashCode();
        }

        @Override
        public void update(final Object target, final Object value) throws ReflectionException {
            //原始对象是参数数组，设置参数值
            if (target != null) {
                ((Object[]) target)[parameter.getIndex()] = value;
            }
        }
    }
}

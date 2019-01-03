package com.jd.laf.binding.converter;

import com.jd.laf.binding.reflect.Generics;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 作用域
 */
public interface Scope {

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
     * 返回目标对象
     *
     * @return
     */
    Object target();

    /**
     * 字段作用域
     */
    class FieldScope implements Scope {
        //字段
        protected final Field field;

        public FieldScope(Field field) {
            this.field = field;
        }

        @Override
        public Annotation[] getAnnotations() {
            return field.getAnnotations();
        }

        @Override
        public Class getGenericType() {
            return Generics.getGenericType(field.getGenericType());
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
    }

    /**
     * 参数作用域
     */
    class ParameterScope implements Scope {
        //方法
        protected final Method method;
        //参数序号
        protected final int argument;
        //注解
        protected final Annotation[] annotations;
        //泛型
        protected final Class genericType;

        public ParameterScope(Method method) {
            this(method, 0);
        }

        public ParameterScope(Method method, int argument) {
            this.method = method;
            this.argument = argument;
            this.annotations = method.getParameterAnnotations()[argument];
            this.genericType = Generics.getGenericType(method.getGenericParameterTypes()[argument]);
        }

        @Override
        public Annotation[] getAnnotations() {
            return annotations;
        }

        @Override
        public Class getGenericType() {
            return genericType;
        }

        @Override
        public Object target() {
            return method;
        }

        public int getArgument() {
            return argument;
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

            if (argument != that.argument) {
                return false;
            }
            return method.equals(that.method);
        }

        @Override
        public int hashCode() {
            int result = method.hashCode();
            result = 31 * result + argument;
            return result;
        }
    }
}

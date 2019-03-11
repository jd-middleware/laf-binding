package com.jd.laf.binding.reflect.java6;

import com.jd.laf.binding.reflect.Generics;
import com.jd.laf.binding.reflect.MethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Java6参数
 */
public class Java6Parameter implements MethodParameter {
    //方法
    protected final Method method;
    protected final int index;
    //名称
    protected String name;
    protected final Class type;
    protected final Class genericType;
    protected final Annotation[] annotations;

    public Java6Parameter(Method method, int index, String name, Class type, Type parameterType, Annotation[] annotations) {
        this.method = method;
        this.index = index;
        this.name = name == null || name.isEmpty() ? "param" + index : name;
        this.type = type;
        this.genericType = Generics.getGenericType(parameterType);
        this.annotations = annotations;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public Class<?> getGenericType() {
        return genericType;
    }

    @Override
    public Annotation[] getAnnotations() {
        return annotations;
    }

    @Override
    public Object target() {
        return method;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Java6Parameter that = (Java6Parameter) o;

        if (index != that.index) {
            return false;
        }
        return method.equals(that.method);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + index;
        return result;
    }
}

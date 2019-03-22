package com.jd.laf.binding.reflect.java6;

import com.jd.laf.binding.reflect.GenericMeta;
import com.jd.laf.binding.reflect.MethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Java6参数
 */
public class Java6Parameter implements MethodParameter {
    //方法
    protected Method method;
    protected int index;
    //名称
    protected String name;
    //参数类型
    protected Class type;
    //泛型信息
    protected GenericMeta[] genericMetas;
    //注解信息
    protected Annotation[] annotations;

    public Java6Parameter(Method method, int index, String name, Class type, Annotation[] annotations, GenericMeta[] genericMetas) {
        this.method = method;
        this.index = index;
        this.name = name == null || name.isEmpty() ? "param" + index : name;
        boolean variable = genericMetas != null && genericMetas.length == 1 && genericMetas[0].isTypeVariable();
        this.type = variable ? genericMetas[0].getClazz() : type;
        this.annotations = annotations;
        this.genericMetas = variable ? new GenericMeta[0] : genericMetas;
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
    public GenericMeta[] getGenericMetas() {
        return genericMetas;
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

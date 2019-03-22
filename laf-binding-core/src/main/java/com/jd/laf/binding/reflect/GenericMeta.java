package com.jd.laf.binding.reflect;

/**
 * 泛型信息
 */
public class GenericMeta {
    //名称
    protected String name;
    //字段或参数的类型
    protected Class scopeType;
    //泛型的类型
    protected Class clazz;
    //是否是类变量
    protected boolean typeVariable;
    //用于方法上的泛型，代表了第几个参数类型就是该泛型类型
    protected int classParameter = -1;

    public GenericMeta(GenericMeta meta, boolean typeVariable) {
        this.name = meta.name;
        this.clazz = meta.clazz;
        this.typeVariable = typeVariable;
        this.scopeType = scopeType;
    }

    public GenericMeta(GenericMeta meta, Class scopeType) {
        this.name = meta.name;
        this.clazz = meta.clazz;
        this.scopeType = scopeType;
    }

    public GenericMeta(String name) {
        this.name = name;
    }

    public GenericMeta(String name, boolean typeVariable) {
        this.name = name;
        this.typeVariable = typeVariable;
    }

    public GenericMeta(String name, Class clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public GenericMeta(String name, Class scopeType, Class clazz) {
        this(name,scopeType,clazz,false);
    }

    public GenericMeta(String name, Class scopeType, Class clazz, boolean typeVariable) {
        this.name = name;
        this.scopeType = scopeType;
        this.clazz = clazz;
        this.typeVariable = typeVariable;
    }

    public String getName() {
        return name;
    }

    public Class getScopeType() {
        return scopeType;
    }

    public Class getClazz() {
        return clazz;
    }

    public boolean isTypeVariable() {
        return typeVariable;
    }

    public int getClassParameter() {
        return classParameter;
    }

    public void setClassParameter(int classParameter) {
        this.classParameter = classParameter;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GenericMeta{");
        sb.append("name='").append(name).append('\'');
        sb.append(", clazz=").append(clazz);
        sb.append(", classParameter=").append(classParameter);
        sb.append('}');
        return sb.toString();
    }
}
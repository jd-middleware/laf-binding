package com.jd.laf.binding.converter;

/**
 * 转换请求类型信息，原始类型和目标类型已经对基本类型进行了封箱操作
 */
public class ConversionType {
    //原始类型
    protected final Class<?> sourceType;
    //目标类型
    protected final Class<?> targetType;
    //作用域
    protected final Scope scope;

    public ConversionType(Class<?> sourceType, Class<?> targetType) {
        this(sourceType, targetType, null);
    }

    public ConversionType(Class<?> sourceType, Class<?> targetType, Scope scope) {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.scope = scope;
    }

    public Class<?> getSourceType() {
        return sourceType;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public Scope getScope() {
        return scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ConversionType that = (ConversionType) o;

        if (!sourceType.equals(that.sourceType)) {
            return false;
        }
        if (!targetType.equals(that.targetType)) {
            return false;
        }
        return scope != null ? scope.equals(that.scope) : that.scope == null;
    }

    @Override
    public int hashCode() {
        int result = sourceType.hashCode();
        result = 31 * result + targetType.hashCode();
        result = 31 * result + (scope != null ? scope.hashCode() : 0);
        return result;
    }
}
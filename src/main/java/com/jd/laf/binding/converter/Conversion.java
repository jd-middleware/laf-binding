package com.jd.laf.binding.converter;

import java.lang.reflect.Field;

/**
 * 转换请求，原始类型和目标类型已经对基本类型进行了封箱操作
 */
public class Conversion {
    //原始类型
    protected final Class<?> sourceType;
    //目标类型
    protected final Class<?> targetType;
    //原始对象
    protected final Object source;
    //数据转换参数
    protected final Object format;
    //字段
    protected final Field field;

    public Conversion(Class<?> sourceType, Class<?> targetType, Object source) {
        this(sourceType, targetType, source, null,null);
    }

    public Conversion(Class<?> sourceType, Class<?> targetType, Object source, Object format) {
        this(sourceType,targetType,source,format,null);
    }

    public Conversion(Class<?> sourceType, Class<?> targetType, Object source, Object format,Field field) {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.source = source;
        this.format = format;
        this.field=field;
    }

    public Class<?> getSourceType() {
        return sourceType;
    }

    public Class<?> getTargetType() {
        return targetType;
    }

    public Object getSource() {
        return source;
    }

    public Object getFormat() {
        return format;
    }

    public Field getField() {
        return field;
    }
}
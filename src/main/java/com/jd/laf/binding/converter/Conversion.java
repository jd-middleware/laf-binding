package com.jd.laf.binding.converter;

/**
 * 转换请求，原始类型和目标类型已经对基本类型进行了封箱操作
 */
public class Conversion extends ConversionType {
    //原始对象
    protected final Object source;
    //数据转换参数
    protected final Object format;

    public Conversion(Class<?> sourceType, Class<?> targetType, Object source) {
        this(sourceType, targetType, source, null, null);
    }

    public Conversion(Class<?> sourceType, Class<?> targetType, Object source, Object format) {
        this(sourceType, targetType, source, format, null);
    }

    public Conversion(Class<?> sourceType, Class<?> targetType, Object source, Object format, Scope scope) {
        super(sourceType, targetType, scope);
        this.source = source;
        this.format = format;
    }

    public Object getSource() {
        return source;
    }

    public Object getFormat() {
        return format;
    }

}
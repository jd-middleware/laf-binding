package com.jd.laf.binding.binder;

import com.jd.laf.binding.annotation.Value;
import com.jd.laf.binding.reflect.exception.ReflectionException;

/**
 * 值绑定
 */
public class ValueBinder implements Binder {

    @Override
    public boolean bind(final Context context) throws ReflectionException {
        if (context == null) {
            return false;
        }
        Value value = (Value) context.getAnnotation();
        //字段名
        String name = value.value() == null || value.value().isEmpty() ? context.getName() : value.value();
        //获取属性值
        Object result = context.evaluate(name);
        if (result == null) {
            //为空，获取默认值
            String defaultValue = value.defaultValue();
            if (defaultValue != null && !defaultValue.isEmpty()) {
                result = defaultValue;
            }
        }
        if (!value.nullable() && result == null) {
            //判断不能为空
            return false;
        }
        return context.bind(result, value.format());
    }

    @Override
    public Class<?> annotation() {
        return Value.class;
    }
}

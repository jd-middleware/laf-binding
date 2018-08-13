package com.jd.laf.binding.binder;

import com.jd.laf.binding.annotation.Value;
import com.jd.laf.binding.reflect.FieldAccessorFactory;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.reflect.Field;

import static com.jd.laf.binding.reflect.Reflect.evaluate;
import static com.jd.laf.binding.reflect.Reflect.set;

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
        FieldAccessorFactory factory = context.getFactory();
        Object target = context.getTarget();
        Object source = context.getSource();
        //字段名
        Field field = context.getField();
        String name = value.value() == null || value.value().isEmpty() ? field.getName() : value.value();
        //获取属性值
        Object result = evaluate(source, name, factory);

        return set(target, field, result, value.format(), factory);
    }

    @Override
    public Class<?> annotation() {
        return Value.class;
    }
}

package com.jd.laf.binding.binder;

import com.jd.laf.binding.annotation.JsonValue;
import com.jd.laf.binding.marshaller.JsonProvider;
import com.jd.laf.binding.marshaller.JsonProviders;
import com.jd.laf.binding.marshaller.Unmarshaller;
import com.jd.laf.binding.reflect.FieldAccessorFactory;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import java.lang.reflect.Field;

import static com.jd.laf.binding.reflect.Reflect.evaluate;
import static com.jd.laf.binding.reflect.Reflect.set;

/**
 * 值绑定
 */
public class JsonValueBinder implements Binder {

    @Override
    public boolean bind(final Context context) throws ReflectionException {
        if (context == null) {
            return false;
        }
        JsonValue value = (JsonValue) context.getAnnotation();
        FieldAccessorFactory factory = context.getFactory();
        Object target = context.getTarget();
        Object source = context.getSource();
        //字段名
        Field field = context.getField();
        String name = value.value() == null || value.value().isEmpty() ? field.getName() : value.value();
        //获取属性值
        Object result = evaluate(source, name, factory);
        if (!value.nullable() && result == null) {
            //判断不能为空
            return false;
        } else if (!(result instanceof CharSequence)) {
            //不是字符串
            return false;
        }
        //获取JSON插件
        JsonProvider plugin = JsonProviders.getPlugin();
        Unmarshaller unmarshaller = plugin == null ? null : plugin.getUnmarshaller();
        if (unmarshaller == null) {
            return false;
        }
        //反序列化
        try {
            result = unmarshaller.unmarshall(result.toString(), field.getType(), value.format());
            return set(target, field, result, value.format(), factory);
        } catch (ReflectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ReflectionException(e.getMessage(), e);
        }
    }

    @Override
    public Class<?> annotation() {
        return JsonValue.class;
    }
}

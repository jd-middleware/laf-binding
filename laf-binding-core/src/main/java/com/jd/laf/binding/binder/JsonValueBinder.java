package com.jd.laf.binding.binder;

import com.jd.laf.binding.annotation.JsonValue;
import com.jd.laf.binding.marshaller.JsonProvider;
import com.jd.laf.binding.marshaller.Unmarshaller;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import static com.jd.laf.binding.Plugin.JSON;

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
        //字段名
        String name = value.value() == null || value.value().isEmpty() ? context.getName() : value.value();
        //获取属性值
        Object result = context.evaluate(name);
        if (!value.nullable() && result == null) {
            //判断不能为空
            return false;
        } else if (!(result instanceof CharSequence)) {
            //不是字符串
            return false;
        }
        //获取JSON插件
        JsonProvider plugin = JSON.get();
        Unmarshaller unmarshaller = plugin == null ? null : plugin.getUnmarshaller();
        if (unmarshaller == null) {
            return false;
        }
        //反序列化
        try {
            result = unmarshaller.unmarshall(result.toString(), context.getType(), value.format());
            return context.bind(result, value.format());
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

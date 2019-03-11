package com.jd.laf.binding.binder;

import com.jd.laf.binding.annotation.XmlValue;
import com.jd.laf.binding.marshaller.Unmarshaller;
import com.jd.laf.binding.marshaller.XmlProvider;
import com.jd.laf.binding.reflect.FieldAccessorFactory;
import com.jd.laf.binding.reflect.exception.ReflectionException;

import static com.jd.laf.binding.Plugin.XML;

/**
 * 值绑定
 */
public class XmlValueBinder implements Binder {

    @Override
    public boolean bind(final Context context) throws ReflectionException {
        if (context == null) {
            return false;
        }
        XmlValue value = (XmlValue) context.getAnnotation();
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
        XmlProvider plugin = XML.get();
        Unmarshaller unmarshaller = plugin == null ? null : plugin.getUnmarshaller();
        if (unmarshaller == null) {
            return false;
        }
        //反序列化
        try {
            result = unmarshaller.unmarshall(result.toString(), context.getType(), null);
            return context.bind(result);
        } catch (ReflectionException e) {
            throw e;
        } catch (Exception e) {
            throw new ReflectionException(e.getMessage(), e);
        }
    }

    @Override
    public Class<?> annotation() {
        return XmlValue.class;
    }
}

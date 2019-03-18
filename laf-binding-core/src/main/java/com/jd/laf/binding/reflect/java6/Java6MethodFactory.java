package com.jd.laf.binding.reflect.java6;

import com.jd.laf.binding.reflect.GenericMeta;
import com.jd.laf.binding.reflect.Generics;
import com.jd.laf.binding.reflect.MethodFactory;
import com.jd.laf.binding.reflect.MethodParameter;
import com.jd.laf.extension.Extension;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Java6方法工厂实现
 */
@Extension("java6")
public class Java6MethodFactory implements MethodFactory {

    @Override
    public List<MethodParameter> getParameters(final Class clazz, final Method method) {
        if (method == null) {
            return null;
        }
        Class<?>[] types = method.getParameterTypes();
        List<MethodParameter> result = new ArrayList<MethodParameter>(types.length);
        if (types.length > 0) {
            Annotation[][] annotations = method.getParameterAnnotations();
            GenericMeta[][] genericMetas = Generics.get(clazz).get(method);
            for (int i = 0; i < types.length; i++) {
                result.add(new Java6Parameter(method, i, null, types[i], annotations[i], genericMetas[i]));
            }
        }

        return result;
    }

    @Override
    public int getParameterCount(final Method method) {
        return method == null ? 0 : method.getParameterTypes().length;
    }
}
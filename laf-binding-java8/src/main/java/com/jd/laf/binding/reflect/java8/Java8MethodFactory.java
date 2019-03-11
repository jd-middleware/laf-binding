package com.jd.laf.binding.reflect.java8;

import com.jd.laf.binding.reflect.MethodFactory;
import com.jd.laf.binding.reflect.MethodParameter;
import com.jd.laf.extension.Extension;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Java8参数构造
 */
@Extension(value = "java8", order = 100)
public class Java8MethodFactory implements MethodFactory {

    @Override
    public List<MethodParameter> getParameters(final Method method) {
        if (method == null) {
            return null;
        }
        int count = method.getParameterCount();
        List<MethodParameter> result = new ArrayList<>(count);
        if (count > 0) {
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                result.add(new Java8Parameter(method, i, parameters[i]));
            }
        }
        return result;
    }

    @Override
    public int getParameterCount(final Method method) {
        return method == null ? 0 : method.getParameterCount();
    }
}

package com.jd.laf.binding.reflect;

import com.jd.laf.extension.Extensible;

import java.lang.reflect.Method;
import java.util.List;

/**
 * 参数工厂
 */
@Extensible("methodFactory")
public interface MethodFactory {

    /**
     * 构建方法参数
     *
     * @param method
     * @return
     */
    List<MethodParameter> getParameters(Method method);

    /**
     * 返回参数数量
     *
     * @param method
     * @return
     */
    int getParameterCount(Method method);

}

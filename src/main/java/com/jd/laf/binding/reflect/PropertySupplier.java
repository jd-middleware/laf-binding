package com.jd.laf.binding.reflect;

import java.lang.reflect.Method;

/**
 * 属性获取器
 */
public interface PropertySupplier {

    /**
     * 获取属性值
     *
     * @param target 对象
     * @param name   属性
     * @return 属性值
     * @throws Exception
     */
    Object get(Object target, String name) throws Exception;

    /**
     * 是否支持该类型的属性值获取
     *
     * @param clazz 类型
     * @return 支持标识
     */
    boolean support(Class<?> clazz);

    /**
     * 根据反射方法来调用
     */
    class MethodSupplier implements PropertySupplier {

        protected Method method;

        public MethodSupplier(Method method) {
            this.method = method;
        }

        @Override
        public Object get(final Object target, final String name) throws Exception {
            return method.invoke(target, name);
        }

        @Override
        public boolean support(Class<?> clazz) {
            return true;
        }
    }
}

package com.jd.laf.binding.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.jd.laf.binding.reflect.Fields.getField;

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


    abstract class AbstractSupplier implements PropertySupplier {

        @Override
        public boolean support(final Class<?> clazz) {
            return true;
        }
    }

    /**
     * 字段属性提供者
     */
    class FieldSupplier implements PropertySupplier {

        protected FieldAccessorFactory factory;

        public FieldSupplier(FieldAccessorFactory factory) {
            this.factory = factory;
        }

        @Override
        public Object get(final Object target, final String name) throws Exception {
            if (factory != null && Character.isJavaIdentifierStart(name.charAt(0))) {
                Field field = getField(target.getClass(), name);
                if (field != null) {
                    return factory.getAccessor(field).get(target);
                }
            }
            return null;
        }

        @Override
        public boolean support(final Class<?> clazz) {
            return true;
        }
    }

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
        public boolean support(final Class<?> clazz) {
            return true;
        }
    }
}
